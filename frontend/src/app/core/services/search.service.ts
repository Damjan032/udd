import { HttpClient } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { Observable } from 'rxjs';
import { environment } from 'src/environments/environment';

@Injectable({
  providedIn: 'root'
})
export class SearchService {

  constructor(
    private http: HttpClient,
  ) { }

  search(body: any, page: any, size: any, searchValue: any): Observable<any> {
    return this.http.post(`${environment.api_url}search` + "?page=" + page + "&size=" + size + "&searchValue=" + searchValue, body, { responseType: "json" });
  }

  advancedSearch(body: any, page: any, size: any): Observable<any> {
    return this.http.post(`${environment.api_url}search` + "?page=" + page + "&size=" + size, body, { responseType: "json" });
  }

  geoSearch(page: any, size: any, place: any, radius: any): Observable<any> {
    return this.http.get(`${environment.api_url}geo-search` + "?place=" + place + "&radius=" + radius + "&page=" + page + "&size=" + size, { responseType: "json" });
  }
}
