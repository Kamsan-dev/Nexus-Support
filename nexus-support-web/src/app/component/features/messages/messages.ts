import { CommonModule, Location } from '@angular/common';
import { ChangeDetectionStrategy, Component, inject, TemplateRef } from '@angular/core';
import { RouterLink } from '@angular/router';
import { AuthStore } from '../../../store/auth.store';
import { MessageStore } from '../../../store/message.store';
import { DialogService } from '@ngneat/dialog';
import { FormBuilder, FormGroup, ReactiveFormsModule, ValidatorFn, Validators } from '@angular/forms';
import { RoleLabel } from '../../../enum/role';

@Component({
  selector: 'app-messages',
  imports: [CommonModule, RouterLink, ReactiveFormsModule],
  templateUrl: './messages.html',
  styleUrl: './messages.scss',
  changeDetection: ChangeDetectionStrategy.OnPush,
})
export class Messages {
  protected store = inject(MessageStore);
  protected authStore = inject(AuthStore);
  private location = inject(Location);
  private fb = inject(FormBuilder);
  private dialogService = inject(DialogService);
  private readonly NEW_MESSAGE_MODAL_ID = 'newMessageModal';

  // form

  modalForm!: FormGroup;
  readonly roleLabel = RoleLabel;

  ngOnInit(): void {
    if (this.authStore.profile()) {
      this.store.loadConversations();
    }
    this.store.loadRecipients();
    this.initModalForm();
  }

  goBack = () => {
    this.location.back();
  };

  onAddNewMessageClick(event: TouchEvent | MouseEvent, template: TemplateRef<HTMLDivElement>) {
    event.stopImmediatePropagation();
    this.dialogService.open(template, { id: this.NEW_MESSAGE_MODAL_ID });
  }

  closeModal() {
    this.dialogService.closeAll();
  }

  onFormClick = (event: TouchEvent | MouseEvent) => {
    event.stopImmediatePropagation();
    this.store.sendMessage(this.modalForm.value);
    this.closeModal();
  };

  private initModalForm(): void {
    this.modalForm = this.fb.group({
      receiverPublicId: ['no_selection', this.receiverValidator],
      content: ['', Validators.required],
    });
  }

  private readonly receiverValidator: ValidatorFn = (control) => (control.value === 'no_selection' ? { required: true } : null);
}
