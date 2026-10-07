import { ChangeDetectionStrategy, Component } from '@angular/core';

@Component({
  selector: 'app-messages',
  imports: [],
  templateUrl: './messages.html',
  styleUrl: './messages.scss',
  changeDetection: ChangeDetectionStrategy.OnPush,
})
export class Messages {}
