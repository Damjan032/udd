import { AbstractControl, ValidationErrors } from '@angular/forms';

export class CustomValidators {

    static confirmedValidator(control: AbstractControl): ValidationErrors | null {
        const password: string = control.get('newPassword').value;
        const confirmPassword: string = control.get('confirmPassword').value;
        if (password !== confirmPassword) {
            control.get('confirmPassword').setErrors({ match: true });
            return ({ match: true });
        }
        return null;
    }

    static commentValidator(control: AbstractControl): ValidationErrors | null {
        const text: string = control.get('text').value;
        const fileName: string = control.get('fileName').value;
        if (text === '' && fileName === '') {
            control.get('text').setErrors({ allEmpty: true });
            control.get('fileName').setErrors({ allEmpty: true });
            return ({ allEmpty: true });
        }
        control.get('text').setErrors(null);
        control.get('fileName').setErrors(null);
        return null;
    }
}
