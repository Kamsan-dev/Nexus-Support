import { CommonModule, Location } from '@angular/common';
import { ChangeDetectionStrategy, Component, inject } from '@angular/core';
import { AuthStore } from '../../../../store/auth.store';
import { MessageStore } from '../../../../store/message.store';
import { ActivatedRoute } from '@angular/router';

@Component({
  selector: 'app-message-detail',
  imports: [CommonModule],
  templateUrl: './message-detail.html',
  styleUrl: './message-detail.scss',
  changeDetection: ChangeDetectionStrategy.OnPush,
})
export class MessageDetail {
  protected readonly store = inject(MessageStore);
  protected readonly authStore = inject(AuthStore);
  private location = inject(Location);
  private activatedRoute = inject(ActivatedRoute);
  private conversationId: string | undefined = undefined;

  ngOnInit(): void {
    this.conversationId = this.activatedRoute.snapshot.paramMap.get('id') ?? undefined;
    if (this.conversationId != null) {
      this.store.loadConversationMessages(this.conversationId);
    }
  }

  goBack = () => {
    this.location.back();
  };
}
