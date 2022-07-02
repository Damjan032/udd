import { BrowserModule } from '@angular/platform-browser';
import { NgModule } from '@angular/core';
import { AppRoutingModule } from './app-routing.module';
import { AppComponent } from './app.component';
import { SharedModule } from './shared/shared.module';
import { CoreModule } from './core/core.module';
import { AgmCoreModule } from '@agm/core';
import { environment } from 'src/environments/environment';
import { BrowserAnimationsModule } from '@angular/platform-browser/animations';
import { FormsModule, ReactiveFormsModule } from '@angular/forms';
import { MatGoogleMapsAutocompleteModule } from '@angular-material-extensions/google-maps-autocomplete';
import { AuthRoutingModule } from './auth/auth-routing.module';
import { MatSnackBarModule } from '@angular/material/snack-bar';
import { HttpClientModule } from '@angular/common/http';
import { HomeComponent } from './home/home.component';
import { BadgeModule, ButtonsModule, CardsModule, IconsModule } from 'angular-bootstrap-md';
import { SimpleSearchComponent } from './simple-search/simple-search.component';
import { AdvancedSearchComponent } from './advanced-search/advanced-search.component';
import { GeoSearchComponent } from './geo-search/geo-search.component';
import { ApplyComponent } from './apply/apply.component';
import { StatisticsComponent } from './statistics/statistics.component';
import { MatButtonModule } from '@angular/material/button';
import { MatCardModule } from '@angular/material/card';
import { MatFormFieldModule } from '@angular/material/form-field';
import { MatInputModule } from '@angular/material/input';
import { MatIconModule } from '@angular/material/icon';
import { MatSelectModule } from '@angular/material/select';
import { MatCheckboxModule } from '@angular/material/checkbox';
import { NgxPaginationModule } from 'ngx-pagination';
import { ResultComponent } from './result/result.component';

@NgModule({
  declarations: [
    AppComponent,
    HomeComponent,
    SimpleSearchComponent,
    AdvancedSearchComponent,
    GeoSearchComponent,
    ApplyComponent,
    StatisticsComponent,
    ResultComponent
  ],
  imports: [
    BrowserModule,
    AppRoutingModule,
    NgxPaginationModule,
    MatSelectModule,
    SharedModule,
    CoreModule,
    BrowserAnimationsModule,
    ReactiveFormsModule,
    FormsModule,
    MatGoogleMapsAutocompleteModule,
    AuthRoutingModule,
    HttpClientModule,
    MatSnackBarModule,
    CardsModule,
    BadgeModule,
    ButtonsModule,
    IconsModule,
    MatCardModule,
    MatButtonModule,
    MatIconModule,
    MatFormFieldModule,
    MatInputModule,
    MatCheckboxModule,
  ],
  providers: [],
  bootstrap: [AppComponent]
})
export class AppModule { }
