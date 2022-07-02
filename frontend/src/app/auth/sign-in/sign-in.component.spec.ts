import { ComponentFixture, fakeAsync, TestBed, tick } from '@angular/core/testing';
import { Router } from '@angular/router';
import { AuthenticationService } from 'src/app/core/services/authentication.service';
import { JwtService } from 'src/app/core/services/jwt.service';
import { Snackbar } from 'src/app/shared/snackbars/snackbar/snackbar';
import { SignInComponent } from './sign-in.component';
import { of } from 'rxjs';
import { FormsModule, ReactiveFormsModule } from '@angular/forms';
import { By } from '@angular/platform-browser';

describe('SignInComponent', () => {
  let component: SignInComponent;
  let fixture: ComponentFixture<SignInComponent>;
  let snackBar: Snackbar;
  let router: Router;
  let jwtService: JwtService;
  let authenticationService: AuthenticationService;

  beforeEach(async () => {
    const authenticationServiceMocked = {
      login: jasmine.createSpy('login').and.returnValue(of({
        accessToken: 'asdfghjkl',
        expiresIn: 123456
      }))
    };
    const routerMocked = jasmine.createSpyObj('router', ['navigate']);
    const jwtServiceMocked = {
      saveToken: jasmine.createSpy('saveToken'),
    };
    const snackBarMocked = {
      success: jasmine.createSpy('success'),
      error: jasmine.createSpy('error')
    };

    await TestBed.configureTestingModule({
      imports: [FormsModule, ReactiveFormsModule],
      declarations: [ SignInComponent ],
      providers: [
        { provide: AuthenticationService, useValue: authenticationServiceMocked },
        { provide: Router, useValue: routerMocked },
        { provide: Snackbar, useValue: snackBarMocked },
        { provide: JwtService, useValue: jwtServiceMocked }
      ]
    });
  });

  beforeEach(() => {
    fixture = TestBed.createComponent(SignInComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();

    authenticationService = TestBed.inject(AuthenticationService);
    router = TestBed.inject(Router);
    snackBar = TestBed.inject(Snackbar);
    jwtService = TestBed.inject(JwtService);
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });

  it('should be initialized', () => {
    component.ngOnInit();
    expect(component.loginForm).toBeDefined();
    expect(component.loginForm.invalid).toBeTruthy();
  });

  it('should be invalid form when submitted and inputs are empty', () => {
    component.loginForm.controls.email.setValue('');
    component.loginForm.controls.password.setValue('');
    component.onSubmit();

    expect(component.loginForm.invalid).toBeTruthy();
    expect(authenticationService.login).toHaveBeenCalledTimes(0);
    expect(jwtService.saveToken).toHaveBeenCalledTimes(0);
    expect(router.navigate).toHaveBeenCalledTimes(0);
    expect(snackBar.error).toHaveBeenCalledTimes(0);

    const emailErrorMsg = fixture.debugElement.query(By.css('#login-email-error-required')).nativeElement;
    expect(emailErrorMsg).toBeDefined();
    expect(emailErrorMsg.innerHTML).toContain('Email is required');

    const passwordErrorMsg = fixture.debugElement.query(By.css('#login-password-error')).nativeElement;
    expect(passwordErrorMsg).toBeDefined();
    expect(passwordErrorMsg.innerHTML).toContain('Password is required');
  });

  it('should be invalid form when submitted and email is empty', () => {
    component.loginForm.controls.email.setValue('');
    component.loginForm.controls.password.setValue('sifra123');
    component.onSubmit();

    expect(component.loginForm.invalid).toBeTruthy();
    expect(authenticationService.login).toHaveBeenCalledTimes(0);
    expect(jwtService.saveToken).toHaveBeenCalledTimes(0);
    expect(router.navigate).toHaveBeenCalledTimes(0);
    expect(snackBar.error).toHaveBeenCalledTimes(0);

    const emailErrorMsg = fixture.debugElement.query(By.css('#login-email-error-required')).nativeElement;
    expect(emailErrorMsg).toBeDefined();
    expect(emailErrorMsg.innerHTML).toContain('Email is required');
  });

  it('should be invalid form when submitted and email is invalid', () => {
    component.loginForm.controls.email.setValue('emailgmail.com');
    component.loginForm.controls.password.setValue('sifra123');
    component.onSubmit();
    fixture.detectChanges();

    expect(component.loginForm.invalid).toBeTruthy();
    expect(authenticationService.login).toHaveBeenCalledTimes(0);
    expect(jwtService.saveToken).toHaveBeenCalledTimes(0);
    expect(router.navigate).toHaveBeenCalledTimes(0);
    expect(snackBar.error).toHaveBeenCalledTimes(0);

    const emailErrorMsg = fixture.debugElement.query(By.css('#login-email-error-invalid')).nativeElement;
    expect(emailErrorMsg).toBeDefined();
    expect(emailErrorMsg.innerHTML).toContain('Email is not valid');
  });

  it('should be invalid form when submitted and password is empty', () => {
    component.loginForm.controls.email.setValue('email@gmail.com');
    component.loginForm.controls.password.setValue('');
    component.onSubmit();

    expect(component.loginForm.invalid).toBeTruthy();
    expect(authenticationService.login).toHaveBeenCalledTimes(0);
    expect(jwtService.saveToken).toHaveBeenCalledTimes(0);
    expect(router.navigate).toHaveBeenCalledTimes(0);
    expect(snackBar.error).toHaveBeenCalledTimes(0);

    const passwordErrorMsg = fixture.debugElement.query(By.css('#login-password-error')).nativeElement;
    expect(passwordErrorMsg).toBeDefined();
    expect(passwordErrorMsg.innerHTML).toContain('Password is required');
  });

  function newEvent(eventName: string, bubbles = false, cancelable = false): CustomEvent<any> {
    const evt = document.createEvent('CustomEvent');
    evt.initCustomEvent(eventName, bubbles, cancelable, null);
    return evt;
  }

  it('should bind data from fields to login form', fakeAsync(() => {
    fixture.detectChanges();
    tick();

    expect(component.loginForm.value.email).not.toBe('email@gmail.com');
    expect(component.loginForm.value.password).not.toBe('sifra123');

    const emailInput = fixture.debugElement.query(By.css('#login_email')).nativeElement;
    emailInput.value = 'email@gmail.com';
    const passwordInput = fixture.debugElement.query(By.css('#login_password')).nativeElement;
    passwordInput.value = 'sifra123';

    emailInput.dispatchEvent(newEvent('input'));
    passwordInput.dispatchEvent(newEvent('input'));

    expect(component.loginForm.value.email).toBe('email@gmail.com');
    expect(component.loginForm.value.password).toBe('sifra123');
  }));

  it('should login when submitted', fakeAsync(() => {
    component.loginForm.controls.email.setValue('email@gmail.com');
    component.loginForm.controls.password.setValue('sifra123');
    component.onSubmit();

    expect(component.loginForm.invalid).toBeFalsy();
    expect(authenticationService.login).toHaveBeenCalledTimes(1);

    tick();

    expect(jwtService.saveToken).toHaveBeenCalledTimes(1);
    expect(router.navigate).toHaveBeenCalledWith(['/']);
    expect(snackBar.error).toHaveBeenCalledTimes(0);
  }));

});
