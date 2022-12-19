import { Injectable } from '@angular/core';
import { WorkSchedule } from '../models/work-schedule.model';

@Injectable({
  providedIn: 'root'
})
export class WorkScheduleService {
  constructor() { }
  getData() {
    return ELEMENT_DATA;
  }
}

const ELEMENT_DATA: WorkSchedule[] = [
  {
    year: 2022,
    month: '12',
    day: '06',
    detail: 'Wybranie tematu projektu',
  },
  {
    year: 2022,
    month: '12',
    day: '09',
    detail: 'Wykonanie strony internetowej',
  },
  {
    year: 2022,
    month: '12',
    day: '19',
    detail: 'Utworzenie repozytorium na GitHub',
  },
  {
    year: 2022,
    month: '12',
    day: '20',
    detail: 'Ustalenie sposobu działania przygotowywanej aplikacji',
  },
];