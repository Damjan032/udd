import { NgModule } from '@angular/core';
import { Routes, RouterModule } from '@angular/router';
import { AdvancedSearchComponent } from './advanced-search/advanced-search.component';
import { ApplyComponent } from './apply/apply.component';
import { NoAuthGuard } from './auth/guards/no-auth.guard';
import { RoleGuard } from './auth/guards/role.guard';
import { GeoSearchComponent } from './geo-search/geo-search.component';
import { HomeComponent } from './home/home.component';
import { PageNotFoundComponent } from './shared/page-not-found/page-not-found.component';
import { SimpleSearchComponent } from './simple-search/simple-search.component';
import { StatisticsComponent } from './statistics/statistics.component';

const routes: Routes = [
  {
    path: '',
    component: HomeComponent,
    canActivate: [NoAuthGuard]
  },
  {
    path: 'apply',
    component: ApplyComponent,
    canActivate: [NoAuthGuard]
  },
  {
    path: 'simple-search',
    component: SimpleSearchComponent,
    canActivate: [RoleGuard]
  },
  {
    path: 'statistics',
    component: StatisticsComponent,
    canActivate: [RoleGuard]
  },
  {
    path: 'advanced-search',
    component: AdvancedSearchComponent,
    canActivate: [RoleGuard]
  },
  {
    path: 'geo-search',
    component: GeoSearchComponent,
    canActivate: [RoleGuard]
  },
  {
    path: 'auth',
    loadChildren: () => import('./auth/auth.module').then(m => m.AuthModule)
  },
  {
    path: '**',
    component: PageNotFoundComponent
  }
];

@NgModule({
  imports: [RouterModule.forRoot(routes, { relativeLinkResolution: 'legacy' })],
  exports: [RouterModule]
})

export class AppRoutingModule { }
