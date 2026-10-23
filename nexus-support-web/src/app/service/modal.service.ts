import { Injectable, ViewContainerRef } from '@angular/core';
import { Observable, Subject, finalize } from 'rxjs';
import { Modal } from '../component/features/modal/modal';

export type ModalType = 'success' | 'danger' | 'warning';

export interface ModalOptions {
  message: string;
  title?: string;
  subtitle?: string;
  type?: ModalType;
}

@Injectable({ providedIn: 'root' })
export class ModalService {
  open(viewRef: ViewContainerRef, options: ModalOptions): Observable<boolean> {
    const modalRef = viewRef.createComponent(Modal);
    const result = new Subject<boolean>();

    modalRef.setInput('message', options.message);
    modalRef.setInput('title', options.title);
    modalRef.setInput('subtitle', options.subtitle);
    modalRef.setInput('type', options.type ?? 'success');

    const closeSub = modalRef.instance.closeEvent.subscribe(() => {
      result.next(false);
      result.complete();
    });

    const submitSub = modalRef.instance.submitEvent.subscribe(() => {
      result.next(true);
      result.complete();
    });

    return result.asObservable().pipe(
      finalize(() => {
        closeSub.unsubscribe();
        submitSub.unsubscribe();
        modalRef.destroy();
      }),
    );
  }
}
