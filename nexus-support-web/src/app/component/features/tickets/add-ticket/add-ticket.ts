import { CommonModule } from '@angular/common';
import { ChangeDetectionStrategy, Component, effect, inject, signal } from '@angular/core';
import { AbstractControl, FormBuilder, FormGroup, ReactiveFormsModule, Validators } from '@angular/forms';
import { FontAwesomeModule } from '@fortawesome/angular-fontawesome';
import { TicketPriorityEnum, TicketPriorityLabel } from '../../../../enum/ticket-priority';
import { TicketTypeEnum, TicketTypeLabel } from '../../../../enum/ticket-type';
import { TicketStore } from '../../../../store/ticket.store';
import { HotToastService } from '@ngxpert/hot-toast';
import { createFormData, formatFileSize } from '../../../../shared/utils/request';

@Component({
  selector: 'app-add-ticket',
  imports: [ReactiveFormsModule, FontAwesomeModule, CommonModule],
  templateUrl: './add-ticket.html',
  styleUrl: './add-ticket.scss',
  changeDetection: ChangeDetectionStrategy.OnPush,
})
export class AddTicket {
  private fb = inject(FormBuilder);
  private toastService = inject(HotToastService);
  //select, options
  protected readonly ticketStore = inject(TicketStore);
  protected readonly ticketTypeLabel = TicketTypeLabel;
  protected readonly ticketPriorityLabel = TicketPriorityLabel;
  protected readonly ticketPriorities = Object.values(TicketPriorityEnum);
  protected readonly ticketTypes = Object.values(TicketTypeEnum);

  //form
  modalForm!: FormGroup;

  //files
  files = signal<{ name: string; size: string }[]>([]);
  filesToSave: File[] = [];

  constructor() {
    effect(() => {
      if (this.ticketStore.ticketCreated()) {
        this.toastService.success('Ticket créé avec succès !');
        this.ticketStore.resetTicketCreated();
      }
    });
  }

  ngOnInit(): void {
    this.initModalForm();
  }

  onSubmitForm = (event: MouseEvent | TouchEvent): void => {
    event.stopImmediatePropagation();
    console.log(this.modalForm.value);
    if (this.modalForm.invalid) {
      this.modalForm.markAllAsDirty;
      return;
    } else {
      const formdata = createFormData('payload', this.modalForm.value, this.filesToSave);
      this.ticketStore.createTicket(formdata);
      this.filesToSave = [];
      this.files.set([]);
      //this.modalForm.reset;
    }
  };

  onFileChange = (files: FileList) => {
    const fileArray: { name: string; size: string }[] = [];
    Array.from(files).forEach((file) => {
      fileArray.push({ name: file.name, size: formatFileSize(file.size) });
      this.filesToSave.push(file);
    });
    this.files.set(fileArray);
  };

  removeFile = (event: TouchEvent | MouseEvent, file: { name: string; size: string }): void => {
    event.stopImmediatePropagation();
    this.files.set([...this.files().filter((currentFile) => currentFile.name != file.name)]);
    this.filesToSave = [...this.filesToSave.filter((currentFile) => currentFile.name != file.name)];
  };

  private initModalForm(): void {
    this.modalForm = this.fb.group({
      title: ['I cannot log in', Validators.required],
      description: [
        'It is a long established fact that a reader will be distracted by the readable content of a page when looking at its layout. ',
        Validators.required,
      ],
      priority: ['', Validators.required],
      type: ['', Validators.required],
    });
  }

  get f(): { [key: string]: AbstractControl } {
    return this.modalForm.controls;
  }
}
