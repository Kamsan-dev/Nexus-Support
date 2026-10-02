import { Component, inject, signal } from '@angular/core';
import { RouterOutlet } from '@angular/router';
import { FaIconLibrary } from '@fortawesome/angular-fontawesome';
import { fontAwesomeIcons } from './shared/icons/font-awesome-icon';

@Component({
  selector: 'app-root',
  imports: [RouterOutlet],
  templateUrl: './app.html',
  styleUrl: './app.scss',
})
export class App {
  protected readonly title = signal('nexus-support-web');
  private faIconLibrary: FaIconLibrary = inject(FaIconLibrary);

  ngOnInit(): void {
    this.faIconLibrary.addIcons(...fontAwesomeIcons);
  }
}
