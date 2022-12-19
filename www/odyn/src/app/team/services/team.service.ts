import { Injectable } from '@angular/core';
import { Team } from '../models/team.model';

@Injectable({
  providedIn: 'root'
})
export class TeamService {
  constructor() { }
  getData() {
    return ELEMENT_DATA;
  }
}

const ELEMENT_DATA: Team[] = [
  {
    fullname: 'Krzysztof Czarkowski',
    role1: 'Opiekun zespołu',
    email: 'kczark@mat.umk.pl',
    technologies: '',
    imageSrc: './assets/images/team/icons8-customer-100.png',
    imageAlt: 'Opiekun zespołu',
  },
  {
    fullname: 'Wojciech Kuźbiński',
    role1: 'Kierownik 🞄 Backend Developer',
    email: 'wojkuzb@mat.umk.pl',
    technologies: 'TBA',
    imageSrc: './assets/images/team/icons8-customer-100.png',
    imageAlt: 'Kierownik zespołu',
  },
  {
    fullname: 'Mateusz Szymczak',
    role1: 'Frontend Developer',
    email: 'mszymczak710@mat.umk.pl',
    technologies: 'TypeScript 🞄 Angular',
    imageSrc: './assets/images/team/icons8-customer-100.png',
    imageAlt: 'Sekretarz zespołu',
  },
  {
    fullname: 'Damian Gałkowski',
    role1: 'Backend Developer',
    email: 'galdam@mat.umk.pl',
    technologies: 'TBA',
    imageSrc: './assets/images/team/icons8-customer-100.png',
    imageAlt: 'Członek zespołu',
  },
  {
    fullname: 'Krzysztof Krzewiński',
    role1: 'Backend Developer',
    email: 'dalek@mat.umk.pl',
    technologies: 'TBA',
    imageSrc: './assets/images/team/icons8-customer-100.png',
    imageAlt: 'Członek zespołu',
  },
  {
    fullname: 'Slava Kushinir',
    role1: 'Backend Developer',
    email: 'kushinir@mat.umk.pl',
    technologies: 'TBA',
    imageSrc: './assets/images/team/icons8-customer-100.png',
    imageAlt: 'Członek zespołu',
  },
  {
    fullname: 'Jakub Orłowski',
    role1: 'Backend Developer',
    email: 'orljak@mat.umk.pl',
    technologies: 'TBA',
    imageSrc: './assets/images/team/icons8-customer-100.png',
    imageAlt: 'Członek zespołu',
  },
];