import { NgModule } from '@angular/core';
import { BrowserModule } from '@angular/platform-browser';

import { AppRoutingModule } from './app-routing.module';
import { AppComponent } from './app.component';
import { BrowserAnimationsModule } from '@angular/platform-browser/animations';
import { FooterComponent } from './footer/footer.component';
import { HeaderComponent } from './header/header.component';
import { MeetingsComponent } from './meetings/meetings.component';
import { MeetingsDialogComponent } from './meetings-dialog/meetings-dialog.component';
import { FinishTimeComponent } from './finish-time/finish-time.component';
import { ProjectInfoComponent } from './project-info/project-info.component';
import { TeamComponent } from './team/team.component';
import { WorkScheduleComponent } from './work-schedule/work-schedule.component';

@NgModule({
  declarations: [
    AppComponent,
    FooterComponent,
    HeaderComponent,
    MeetingsComponent,
    MeetingsDialogComponent,
    FinishTimeComponent,
    ProjectInfoComponent,
    TeamComponent,
    WorkScheduleComponent
  ],
  imports: [
    BrowserModule,
    AppRoutingModule,
    BrowserAnimationsModule
  ],
  providers: [],
  bootstrap: [AppComponent]
})
export class AppModule { }
