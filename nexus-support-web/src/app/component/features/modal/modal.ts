import { CommonModule } from '@angular/common';
import { ChangeDetectionStrategy, Component, ElementRef, inject, input, output } from '@angular/core';
import { FontAwesomeModule } from '@fortawesome/angular-fontawesome';

@Component({
  selector: 'app-modal',
  imports: [CommonModule, FontAwesomeModule],
  templateUrl: './modal.html',
  styleUrl: './modal.scss',
  changeDetection: ChangeDetectionStrategy.OnPush,
})
export class Modal {
  title = input<string>();
  message = input<string>();
  subtitle = input<string>();
  type = input<'success' | 'warning' | 'danger'>('success');
  closeEvent = output<void>();
  submitEvent = output<Event>();

  close = () => {
    this.closeEvent.emit();
  };

  submit = (event: Event) => {
    this.submitEvent.emit(event);
  };
}
