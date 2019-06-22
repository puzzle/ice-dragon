import { Injectable } from '@angular/core';
import { NgbModal, NgbModalRef } from '@ng-bootstrap/ng-bootstrap';
import { JhiSubscribeModalComponent } from './subscribe.component';
import { IcedragonService } from 'app/icedragon/icedragon.service';
import { Platform } from 'app/icedragon/platform.model';
import { InvoiceService } from 'app/icedragon/invoice.service';

@Injectable({ providedIn: 'root' })
export class SubscribeDialogService {
  private isOpen = false;

  constructor(private modalService: NgbModal, private service: IcedragonService, private invoiceService: InvoiceService) {}

  openDialog(platform: Platform, paid = false): NgbModalRef {
    if (this.isOpen) {
      return;
    }
    this.isOpen = true;
    this.invoiceService.connect(() => null);
    const modalRef = this.modalService.open(JhiSubscribeModalComponent, { backdrop: 'static' });
    modalRef.componentInstance.platform = platform;

    if (paid) {
      modalRef.componentInstance.paid = true;
    }

    modalRef.result.then(
      result => {
        this.isOpen = false;
        this.invoiceService.disconnect();
      },
      reason => {
        this.isOpen = false;
        this.invoiceService.disconnect();
      }
    );
    return modalRef;
  }
}
