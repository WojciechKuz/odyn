import {
  ChangeDetectionStrategy,
  ChangeDetectorRef,
  Component,
  Inject,
  Injectable,
  OnDestroy,
} from '@angular/core';
import {
  MatCalendar,
  MatCalendarCellCssClasses,
} from '@angular/material/datepicker';
import { MatDialog, MatDialogConfig } from '@angular/material/dialog';
import { MeetingsService } from './services/meetings.service';
import { Meetings } from './models/meetings.model';
import { MeetingsDialogComponent } from '../meetings-dialog/meetings-dialog.component';
import { Subject, takeUntil } from 'rxjs';
import {
  DateAdapter,
  MAT_DATE_FORMATS,
  MatDateFormats,
} from '@angular/material/core';
import { LOCALE_ID } from '@angular/core';
import { NativeDateAdapter } from '@angular/material/core';

@Injectable()
export class CustomDateAdapter extends NativeDateAdapter {
  constructor(@Inject(LOCALE_ID) public override locale: any) {
    super(locale);
    console.log(locale);
  }
  override getFirstDayOfWeek() {
    return 1;
  }

  override getDayOfWeekNames(style: 'long' | 'short' | 'narrow'): string[] {
    return ['Niedz', 'Pon', 'Wt', 'Śr', 'Czw', 'Pt', 'Sob'];
  }
}
@Component({
  selector: 'app-meetings',
  templateUrl: './meetings.component.html',
  styleUrls: ['./meetings.component.scss'],
})
export class MeetingsComponent {
  customHeader = CustomHeader;
  selectedDate: Date | null;
  minDate = new Date('2022/11/01');
  maxDate = new Date('2023/06/01');
  dataSource: Meetings[];
  meet: Meetings;
  constructor(public dialog: MatDialog, private dataService: MeetingsService) {}

  ngOnInit() {
    this.dataSource = this.dataService.getData();
  }

  public getFirstDayOfWeek(): number {
    return 1;
  }

  disableWeekendsFilter = (d: Date): boolean => {
    return d.getDay() !== 0 && d.getDay() !== 6;
  };

  dateClass() {
    return (date: Date): MatCalendarCellCssClasses => {
      const highlightDate = this.dataSource
        .map((meet) => new Date(meet.date))
        .some(
          (d) =>
            d.getDate() === date.getDate() &&
            d.getMonth() === date.getMonth() &&
            d.getFullYear() === date.getFullYear()
        );

      return highlightDate ? 'special-date' : '';
    };
  }

  isSelectedValidDate = (date: Date): boolean => {
    const highlightDate = this.dataSource
      .map((meet) => new Date(meet.date))
      .some(
        (d) =>
          d.getDate() === date.getDate() &&
          d.getMonth() === date.getMonth() &&
          d.getFullYear() === date.getFullYear()
      );

    if (!highlightDate) return false;
    else return true;
  };

  openDialog(value: Meetings): void {
    const dialogConfig = new MatDialogConfig();

    dialogConfig.data = value;
    dialogConfig.disableClose = true;
    dialogConfig.enterAnimationDuration = '500ms';

    const dialogRef = this.dialog.open(MeetingsDialogComponent, dialogConfig);
    dialogRef.afterClosed().subscribe((result) => {
      console.log('The dialog was closed' + result);
    });
  }

  onSelectedChange(event: Date) {
    let meetingInfo = document.getElementsByClassName(
      'span-info'
    )[0] as HTMLElement;
    if (!this.isSelectedValidDate(this.selectedDate)) {
      meetingInfo.style.opacity = '1';
      meetingInfo.textContent = 'Brak wydarzeń tego dnia!';
      setTimeout(function () {
        meetingInfo.style.opacity = '0';
      }, 2000);
    } else {
      meetingInfo.style.opacity = '0';
      for (let item of this.dataSource) {
        if (event.getDate() === new Date(item.date).getDate()) {
          this.openDialog(item);
          this.selectedDate = null;
        }
      }
    }
  }
}

@Component({
  selector: 'custom-header',
  styles: [
    `
      .custom-header {
        display: flex;
        align-items: center;
        padding: 6px;
      }

      .custom-header-label {
        flex: 1;
        font-weight: 500;
        text-align: center;
      }

      @media screen and (max-width: 480px) {
        .custom-header {
          display: flex;
          align-items: center;
          justify-content: space-around;
        }

        .custom-header-label {
          font-size: 12px;
        }

        .custom-header .mat-icon {
          font-size: 16px;
        }
      }
    `,
  ],
  template: `
    <div class="custom-header">
      <button mat-icon-button (click)="previousClicked('year')">
        <mat-icon>keyboard_double_arrow_left</mat-icon>
      </button>
      <button mat-icon-button (click)="previousClicked('month')">
        <mat-icon>keyboard_arrow_left</mat-icon>
      </button>
      <span class="custom-header-label">{{ periodLabel }}</span>
      <button mat-icon-button (click)="nextClicked('month')">
        <mat-icon>keyboard_arrow_right</mat-icon>
      </button>
      <button mat-icon-button (click)="nextClicked('year')">
        <mat-icon>keyboard_double_arrow_right</mat-icon>
      </button>
    </div>
  `,
  changeDetection: ChangeDetectionStrategy.OnPush,
})
export class CustomHeader<D> implements OnDestroy {
  private _destroyed = new Subject<void>();

  constructor(
    private _calendar: MatCalendar<D>,
    private _dateAdapter: DateAdapter<D>,
    @Inject(MAT_DATE_FORMATS) private _dateFormats: MatDateFormats,
    cdr: ChangeDetectorRef
  ) {
    _calendar.stateChanges
      .pipe(takeUntil(this._destroyed))
      .subscribe(() => cdr.markForCheck());
  }

  ngOnDestroy() {
    this._destroyed.next();
    this._destroyed.complete();
  }

  get periodLabel() {
    return this._dateAdapter
      .format(
        this._calendar.activeDate,
        this._dateFormats.display.monthYearA11yLabel
      )
      .toLocaleUpperCase('pl-PL');
  }

  previousClicked(mode: 'month' | 'year') {
    this._calendar.activeDate =
      mode === 'month'
        ? this._dateAdapter.addCalendarMonths(this._calendar.activeDate, -1)
        : this._dateAdapter.addCalendarYears(this._calendar.activeDate, -1);
  }

  nextClicked(mode: 'month' | 'year') {
    this._calendar.activeDate =
      mode === 'month'
        ? this._dateAdapter.addCalendarMonths(this._calendar.activeDate, 1)
        : this._dateAdapter.addCalendarYears(this._calendar.activeDate, 1);
  }
}
