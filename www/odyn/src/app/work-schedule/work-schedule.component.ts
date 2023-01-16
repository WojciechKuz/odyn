import { Component } from '@angular/core';
import * as AOS from 'aos';
import { WorkSchedule } from './models/work-schedule.model';
import { WorkScheduleService } from './services/work-schedule.service';

@Component({
  selector: 'app-work-schedule',
  templateUrl: './work-schedule.component.html',
  styleUrls: ['./work-schedule.component.scss'],
})
export class WorkScheduleComponent {
  events: WorkSchedule[];

  constructor(private dataService: WorkScheduleService) {}

  ngOnInit() {
    this.events = this.dataService.getData();

    AOS.init({
      disable: function () {
        let maxWidth = 0;
        return window.innerWidth < maxWidth;
      },
    });
  }
}
