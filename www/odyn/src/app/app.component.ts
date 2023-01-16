import { Component } from '@angular/core';
import { ActivatedRoute } from '@angular/router';
import * as AOS from 'aos';
@Component({
  selector: 'app-root',
  templateUrl: './app.component.html',
  styleUrls: ['./app.component.scss'],
})
export class AppComponent {
  title = 'odyn';

  constructor(private activatedRoute: ActivatedRoute) {}

  navigateTo(section: any) {
    document.getElementById(section)?.scrollIntoView({ behavior: 'smooth' });
  }

  ngOnInit() {
    AOS.init({
      disable: function () {
        let maxWidth = 1024;
        return window.innerWidth < maxWidth;
      },
    });

    this.activatedRoute.fragment.subscribe((value) => {
      this.navigateTo(value);
    });
  }
}
