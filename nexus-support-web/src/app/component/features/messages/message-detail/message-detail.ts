import { ChangeDetectionStrategy, Component } from '@angular/core';

@Component({
  selector: 'app-message-detail',
  imports: [],
  templateUrl: './message-detail.html',
  styleUrl: './message-detail.scss',
  changeDetection: ChangeDetectionStrategy.OnPush,
})
export class MessageDetail {}
