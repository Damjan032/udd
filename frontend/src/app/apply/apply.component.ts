import { HttpHeaders } from '@angular/common/http';
import { Component, OnInit } from '@angular/core';
import { AbstractControl, FormBuilder, FormGroup, Validators } from '@angular/forms';
import { Router } from '@angular/router';
import { MyErrorStateMatcher } from 'src/app/core/error-matchers/ErrorStateMatcher';
import { Snackbar } from 'src/app/shared/snackbars/snackbar/snackbar';
import { Apply } from '../core/models/request/apply.model';
import { JobService } from '../core/services/job.service';

@Component({
  selector: 'app-apply',
  templateUrl: './apply.component.html',
  styleUrls: ['./apply.component.scss']
})
export class ApplyComponent implements OnInit {
  applyForm: FormGroup;
  submitted = false;
  matcher: MyErrorStateMatcher = new MyErrorStateMatcher();
  uploadedFile: string | ArrayBuffer = '';

  constructor(
    private formBuilder: FormBuilder,
    private snackBar: Snackbar,
    private router: Router,
    private jobService: JobService
  ) { }

  ngOnInit(): void {
    this.applyForm = this.formBuilder.group({
      firstName: ['', Validators.required],
      lastName: ['', Validators.required],
      email: ['', [Validators.required, Validators.email]],
      city: ['', Validators.required],
      country: ['', Validators.required],
      educationDegree: ['', Validators.required],
      file: [null, Validators.required],
    });

    if (navigator.geolocation) {
      this.getPosition().then(pos => {
        console.log(`Positon: ${pos.lng} ${pos.lat}`);
        this.jobService.logApplication(pos.lat, pos.lng).subscribe(
          (data: any) => {
            console.log(data);
          },
          err => {
            console.log(err.error);
          });
      });
    } else {
      console.log("Geolocation is not supported by this browser.");
    }
  }

  get f(): { [key: string]: AbstractControl; } { return this.applyForm.controls; }

  onSubmit(): void {
    this.submitted = true;
    if (this.applyForm.invalid) {
      return;
    }
    const uploadData = new FormData();
    uploadData.append('firstName', this.applyForm.get('firstName').value);
    uploadData.append('lastName', this.applyForm.get('lastName').value);
    uploadData.append('email', this.applyForm.get('email').value);
    uploadData.append('city', this.applyForm.get('city').value);
    uploadData.append('country', this.applyForm.get('country').value);
    uploadData.append('educationDegree', this.applyForm.get('educationDegree').value);
    uploadData.append('cv', this.applyForm.get('file').value, this.applyForm.get('file').value.name);
    let headers = new HttpHeaders();
    var options = {
      content: uploadData, headers: headers, responseType: 'text'
    }

    this.jobService.post(uploadData, options).subscribe(
      (data: string) => {
        console.log(data);
        this.succesMessage('Succesfully applied for job!');
        this.goBack();
      },
      err => {
        console.log(err.error);
        this.errorMessage(err.error);
      });
  }

  chooseFile(event): void {
    let file = event.target.files[0];
    this.applyForm.patchValue({
      file
    });
  }

  private goBack(): void {
    this.router.navigateByUrl('/');
  }

  private succesMessage(message: string): void {
    this.snackBar.success(message);
  }

  private errorMessage(message: string): void {
    this.snackBar.error(message);
  }

  getPosition(): Promise<any> {
    return new Promise((resolve, reject) => {

      navigator.geolocation.getCurrentPosition(resp => {

        resolve({ lng: resp.coords.longitude, lat: resp.coords.latitude });
      },
        err => {
          reject(err);
        });
    });

  }

}
