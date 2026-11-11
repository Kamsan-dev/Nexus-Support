import { ChangeDetectionStrategy, Component, inject } from '@angular/core';
import { MessageStore } from '../../../store/message.store';
import { AuthStore } from '../../../store/auth.store';
import { CommonModule, Location } from '@angular/common';
import { ActivatedRoute, RouterLink } from '@angular/router';

@Component({
  selector: 'app-messages',
  imports: [CommonModule, RouterLink],
  templateUrl: './messages.html',
  styleUrl: './messages.scss',
  changeDetection: ChangeDetectionStrategy.OnPush,
})
export class Messages {
  protected store = inject(MessageStore);
  protected authStore = inject(AuthStore);
  private location = inject(Location);

  goBack = () => {
    this.location.back();
  };
}
