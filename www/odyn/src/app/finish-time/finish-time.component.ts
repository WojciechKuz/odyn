import { Component, ElementRef, ViewChild } from '@angular/core';

@Component({
  selector: 'app-finish-time',
  templateUrl: './finish-time.component.html',
  styleUrls: ['./finish-time.component.scss'],
})
export class FinishTimeComponent {
  date: any;
  now: any;
  targetDate: any = new Date('2023/06/01');
  targetTime: any = this.targetDate.getTime();
  difference: number;
  @ViewChild('weeks', { static: true }) weeks: ElementRef;
  @ViewChild('days', { static: true }) days: ElementRef;
  @ViewChild('hours', { static: true }) hours: ElementRef;
  @ViewChild('minutes', { static: true }) minutes: ElementRef;
  @ViewChild('seconds', { static: true }) seconds: ElementRef;

  ngAfterViewInit() {
    setInterval(() => {
      this.tickTock();
      this.difference = this.targetTime - this.now;
      this.difference = this.difference / (1000 * 60 * 60 * 24 * 7);

      if (!isNaN(this.weeks.nativeElement.innerText)) {
        Math.floor(this.difference) < 10
          ? (this.weeks.nativeElement.innerText =
              '0' + Math.floor(this.difference))
          : (this.weeks.nativeElement.innerText = Math.floor(this.difference));
      } else {
        this.weeks.nativeElement.innerHTML = `<img src="https://i.gifer.com/VAyR.gif" />`;
      }

      if (!isNaN(this.days.nativeElement.innerText)) {
        Math.floor(this.difference / 7) < 10
          ? (this.days.nativeElement.innerText =
              '0' + Math.floor(this.difference / 7))
          : (this.days.nativeElement.innerText = Math.floor(
              this.difference / 7
            ));
      } else {
        this.days.nativeElement.innerHTML = `<img src="https://i.gifer.com/VAyR.gif" />`;
      }
    }, 1000);
  }

  tickTock() {
    this.date = new Date();
    this.now = this.date.getTime();

    Math.floor(this.difference) < 10
      ? (this.weeks.nativeElement.innerText = '0' + Math.floor(this.difference))
      : (this.weeks.nativeElement.innerText = Math.floor(this.difference));

    Math.floor(this.difference / 7) < 10
      ? (this.days.nativeElement.innerText =
          '0' + Math.floor(this.difference / 7))
      : (this.days.nativeElement.innerText = Math.floor(this.difference / 7));

    this.hours.nativeElement.innerText = 23 - this.date.getHours();

    59 - this.date.getMinutes() < 10
      ? (this.minutes.nativeElement.innerText =
          '0' + (59 - this.date.getMinutes()))
      : (this.minutes.nativeElement.innerText = 59 - this.date.getMinutes());

    59 - this.date.getSeconds() < 10
      ? (this.seconds.nativeElement.innerText =
          '0' + (59 - this.date.getSeconds()))
      : (this.seconds.nativeElement.innerText = 59 - this.date.getSeconds());
  }
}
