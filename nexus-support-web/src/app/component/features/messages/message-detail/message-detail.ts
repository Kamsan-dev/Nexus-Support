import { CommonModule, Location } from '@angular/common';
import { ChangeDetectionStrategy, ChangeDetectorRef, Component, effect, ElementRef, inject, Renderer2, signal, viewChild } from '@angular/core';
import { FormsModule } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { AuthStore } from '../../../../store/auth.store';
import { MessageStore } from '../../../../store/message.store';

@Component({
  selector: 'app-message-detail',
  imports: [CommonModule, FormsModule],
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
  messageRef = viewChild<ElementRef<HTMLDivElement>>('message');
  private renderer = inject(Renderer2);
  private changeDetector = inject(ChangeDetectorRef);

  //form
  messageContent = signal('');

  constructor() {
    effect(() => {
      if (this.store.messages()) {
        this.scrollChatWindow();
      }
    });
  }

  ngOnInit(): void {
    this.conversationId = this.activatedRoute.snapshot.paramMap.get('id') ?? undefined;
    if (this.conversationId != null) {
      this.store.loadConversationMessages(this.conversationId);
    }
  }

  goBack = () => {
    this.location.back();
  };

  onSendMessage = () => {
    if (this.store.correspondentPublicId() === null) return;
    this.store.sendMessageInConversation({
      conversationId: this.conversationId!,
      payload: {
        receiverPublicId: this.store.correspondentPublicId()!,
        content: this.messageContent(),
      },
    });
    this.messageContent.update((m) => '');
  };

  private scrollChatWindow() {
    if (this.messageRef() && this.messageRef()!.nativeElement) {
      if (this.messageRef()!.nativeElement.scrollHeight > this.messageRef()!.nativeElement.scrollTop) {
        this.renderer.setProperty(this.messageRef()?.nativeElement, 'scrollTop', this.messageRef()?.nativeElement.scrollHeight);
      }
    }
  }
}
