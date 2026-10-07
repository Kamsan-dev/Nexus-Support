import { CommonModule } from '@angular/common';
import {
  ChangeDetectionStrategy,
  Component,
  ElementRef,
  HostListener,
  inject,
  signal,
  TemplateRef,
  ViewChild,
} from '@angular/core';
import { RouterLink, RouterLinkActive } from '@angular/router';
import { FontAwesomeModule } from '@fortawesome/angular-fontawesome';
import { DialogService } from '@ngneat/dialog';

@Component({
  selector: 'app-navbar',
  imports: [FontAwesomeModule, RouterLink, CommonModule, RouterLinkActive],
  templateUrl: './navbar.html',
  styleUrl: './navbar.scss',
  changeDetection: ChangeDetectionStrategy.OnPush,
})
export class Navbar {
  private element = inject(ElementRef);
  private dialogService = inject(DialogService);
  private readonly TICKET_MODAL_ID = 'ticketModal';
  @ViewChild('addFormTicket') addTicketTemplate!: TemplateRef<any>;
  isNavOpen = signal(false);
  isMenuOpen = signal(false);

  toggleNav = () => {
    this.isNavOpen.update(() => !this.isNavOpen());
  };
  toggleMenu = () => {
    this.isMenuOpen.update(() => !this.isMenuOpen());
  };

  onAddNewTicketClick(event: TouchEvent | MouseEvent, template: TemplateRef<HTMLDivElement>) {
    event.stopImmediatePropagation();
    this.dialogService.open(template, { id: this.TICKET_MODAL_ID });
  }

  closeModal() {
    this.dialogService.closeAll();
  }

  @HostListener('document:click', ['$event'])
  onDocumentClick(event: MouseEvent) {
    const target = event.target as HTMLElement;
    if (
      !this.element.nativeElement.querySelector('.usermenu')?.contains(target) &&
      this.isMenuOpen()
    ) {
      this.isMenuOpen.update(() => false);
    }
    if (
      !this.element.nativeElement.querySelector('.navmenu')?.contains(target) &&
      this.isNavOpen()
    ) {
      this.isNavOpen.update(() => false);
    }
  }
}
