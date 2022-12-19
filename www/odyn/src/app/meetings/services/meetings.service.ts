import { Injectable } from '@angular/core';
import { Meetings } from '../models/meetings.model';

@Injectable({
  providedIn: 'root'
})
export class MeetingsService {
  constructor() { }
  getData() {
    return ELEMENT_DATA;
  }
}

const ELEMENT_DATA: Meetings[] = [
  {
    position: 0,
    date: '2022-11-07',
    start_time: '18:00',
    finish_time: '19:30',
    description1: 'Spotkanie organizacyjne z koordynatorem przedmiotu',
    description2: '(omówienie regulaminu przedmiotu, utworzenie zespołów)',
  },
  {
    position: 1,
    date: '2022-11-14',
    start_time: '16:00',
    finish_time: '16:45',
    description1: 'Spotkanie z opiekunem zespołu',
    description2: '(omówienie zasad zaliczenia, sprawy organizacyjne)',
  },
  {
    position: 2,
    date: '2022-11-22',
    start_time: '18:00',
    finish_time: '19:00',
    description1: 'Spotkanie z opiekunem zespołu',
    description2: '(przedstawienie kilku propozycji aplikacji)',
  },
  {
    position: 3,
    date: '2022-12-06',
    start_time: '18:00',
    finish_time: '19:30',
    description1: 'Spotkanie z opiekunem zespołu',
    description2: '(przedstawienie kilku propozycji aplikacji)',
  },
];