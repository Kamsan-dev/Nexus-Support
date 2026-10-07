import { ChangeDetectionStrategy, Component } from '@angular/core';
import { RouterLink, RouterOutlet } from '@angular/router';
import { Navbar } from './navbar/navbar';

@Component({
  selector: 'app-layout',
  imports: [RouterOutlet, Navbar, RouterLink],
  templateUrl: './layout.html',
  styleUrl: './layout.scss',
  changeDetection: ChangeDetectionStrategy.OnPush,
})
export class Layout {}
