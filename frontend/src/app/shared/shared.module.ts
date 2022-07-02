import { NgModule } from '@angular/core';
import { CommonModule } from '@angular/common';
import { HeaderComponent } from './layout/header/header.component';
import { FooterComponent } from './layout/footer/footer.component';
import { ButtonsModule, IconsModule, NavbarModule } from 'angular-bootstrap-md';
import { SearchComponent } from './search/search.component';
import { ReactiveFormsModule } from '@angular/forms';
import { AgmCoreModule } from '@agm/core';
import { RouterModule } from '@angular/router';
import { environment } from 'src/environments/environment';
import { NgxPaginationModule } from 'ngx-pagination';
import { LightboxModule } from 'ngx-lightbox';
import { ConfirmationDialogComponent } from './dialogs/confirmation-dialog/confirmation-dialog.component';
import { Snackbar } from './snackbars/snackbar/snackbar';
import { MatMenuModule } from '@angular/material/menu';
import { MatButtonModule } from '@angular/material/button';
import { MatIconModule } from '@angular/material/icon';
import { MatDialogModule } from '@angular/material/dialog';
import { MatFormFieldModule } from '@angular/material/form-field';
import { MatInputModule } from '@angular/material/input';
import { PageNotFoundComponent } from './page-not-found/page-not-found.component';

@NgModule({
  declarations: [
    HeaderComponent,
    FooterComponent,
    SearchComponent,
    ConfirmationDialogComponent,
    PageNotFoundComponent
  ],
  imports: [
    CommonModule,
    NavbarModule,
    IconsModule,
    ReactiveFormsModule,
    RouterModule,
    MatMenuModule,
    MatButtonModule,
    MatIconModule,
    MatFormFieldModule,
    MatInputModule,
    ButtonsModule,
    NgxPaginationModule,
    LightboxModule,
    MatDialogModule,
  ],
  exports: [
    HeaderComponent,
    FooterComponent,
    SearchComponent,
    ConfirmationDialogComponent,
  ],
  providers: [Snackbar]
})
export class SharedModule { }
