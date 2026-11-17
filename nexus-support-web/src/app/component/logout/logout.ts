import { ChangeDetectionStrategy, Component, inject } from '@angular/core';
import { StorageService } from '../../service/storage.service';
import { Router, RouterLink } from '@angular/router';
import { OAuthStorageService } from '../../auth/oauthstorage.service';
import { FontAwesomeModule } from '@fortawesome/angular-fontawesome';

@Component({
  selector: 'app-logout',
  imports: [FontAwesomeModule, RouterLink],
  templateUrl: './logout.html',
  changeDetection: ChangeDetectionStrategy.OnPush,
})
export class Logout {
  private storage = inject(StorageService);
  private oauthStorage = inject(OAuthStorageService);
  private router = inject(Router);

  ngOnInit(): void {
    this.storage.clear();
    this.oauthStorage.clear();

    void this.router.navigateByUrl('/', { replaceUrl: true });
  }
}
