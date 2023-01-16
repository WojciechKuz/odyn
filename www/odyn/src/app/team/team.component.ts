import { Component } from '@angular/core';
import { TeamService } from './services/team.service';
import { Team } from './models/team.model';

@Component({
  selector: 'app-team',
  templateUrl: './team.component.html',
  styleUrls: ['./team.component.scss'],
})
export class TeamComponent {
  teamMembers: Team[];

  constructor(private dataService: TeamService) {}

  ngOnInit() {
    this.teamMembers = this.dataService.getData();
  }
}
