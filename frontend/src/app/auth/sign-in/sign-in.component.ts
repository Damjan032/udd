import { Component, OnInit } from '@angular/core';
import { AbstractControl, FormBuilder, FormGroup, Validators } from '@angular/forms';
import { Router } from '@angular/router';
import { UserLogin } from 'src/app/core/models/request/user-login-request.models';
import { AuthenticationService } from 'src/app/core/services/authentication.service';
import { JwtService } from 'src/app/core/services/jwt.service';
import { MyErrorStateMatcher } from 'src/app/core/error-matchers/ErrorStateMatcher';
import { Snackbar } from 'src/app/shared/snackbars/snackbar/snackbar';
import { UserTokenState } from 'src/app/core/models/response/user-token-state.model';

@Component({
  selector: 'app-sign-in',
  templateUrl: './sign-in.component.html',
  styleUrls: ['./sign-in.component.scss']
})
export class SignInComponent implements OnInit {
  loginForm: FormGroup;
  submitted = false;
  hidePassword = true;
  matcher: MyErrorStateMatcher = new MyErrorStateMatcher();

  constructor(
    private formBuilder: FormBuilder,
    private snackBar: Snackbar,
    private router: Router,
    private jwtService: JwtService,
    private authenticationService: AuthenticationService
  ) { }

  ngOnInit(): void {
    this.loginForm = this.formBuilder.group({
      email: ['', [Validators.required, Validators.email]],
      password: ['', Validators.required]
    });
  }

  get f(): { [key: string]: AbstractControl; } { return this.loginForm.controls; }

  onSubmit(): void {
    this.submitted = true;
    if (this.loginForm.invalid) {
      return;
    }
    const credentials: UserLogin = { email: '', password: '' };
    credentials.email = this.loginForm.value.email;
    credentials.password = this.loginForm.value.password;
    this.authenticationService.login(credentials).subscribe((data: UserTokenState) => {
      this.jwtService.saveToken(data);
      this.router.navigate(['/simple-search']);
    },
      error => {
        this.snackBar.error(error.error);
      });
  }

}
