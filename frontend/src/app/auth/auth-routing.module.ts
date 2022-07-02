import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';
import { NoAuthGuard } from './guards/no-auth.guard';
import { SignInComponent } from './sign-in/sign-in.component';

const routes: Routes = [
    {
        path: 'sign-in',
        component: SignInComponent,
        canActivate: [NoAuthGuard]
    },
];


@NgModule({
    imports: [RouterModule.forChild(routes)],
    exports: [RouterModule]
})

export class AuthRoutingModule { }
