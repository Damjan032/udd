import { HttpClient, HttpHeaders } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { Observable } from 'rxjs';
import { environment } from 'src/environments/environment';
import { Apply } from '../models/request/apply.model';
import { Job } from '../models/response/job.model';
import { map } from 'rxjs/operators';

@Injectable({
  providedIn: 'root'
})
export class JobService {

  constructor(
    private http: HttpClient,
  ) { }

  getAll(): Observable<Job[]> {
    return this.http.get<Job[]>(`${environment.api_url}jobs`, { responseType: 'json' });
  }

  post(body: any, options: any): Observable<any> {
    return this.http.post(`${environment.api_url}elastic/upload-index`, body, options);
  }

  logApplication(latitude: any, longitude: any) {
    return this.http.get(`${environment.api_url}jobs/log-application?lat=${latitude}&lon=${longitude}`);
  }

  getCv(cvPath: string) {
    console.log(cvPath);
    let body = {
      path: cvPath,
    }
    return this.http.post(`${environment.api_url}jobs/cv`, body, {
      responseType: 'blob',
      observe: 'response',
    })
      .pipe(
        map((res: any) => {
          return new Blob([res.body], { type: 'application/pdf' });
        })
      );
  }

}
