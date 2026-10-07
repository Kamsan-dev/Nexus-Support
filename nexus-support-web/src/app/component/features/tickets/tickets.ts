import { ChangeDetectionStrategy, Component } from '@angular/core';

@Component({
  selector: 'app-tickets',
  imports: [],
  templateUrl: './tickets.html',
  styleUrl: './tickets.scss',
  changeDetection: ChangeDetectionStrategy.OnPush,
})
export class Tickets {}
