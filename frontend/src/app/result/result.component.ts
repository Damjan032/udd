import { Component, Input, OnInit } from '@angular/core';
import { JobService } from '../core/services/job.service';
import { Output, EventEmitter } from '@angular/core';

@Component({
  selector: 'app-result',
  templateUrl: './result.component.html',
  styleUrls: ['./result.component.scss']
})
export class ResultComponent implements OnInit {
  @Input() result: any = {};
  @Input() visited: boolean = false;

  constructor(private jobService: JobService) { }

  ngOnInit(): void { }

  displayCV() {
    if (localStorage.getItem('visitedLinks')) {
      let all: number[] = JSON.parse(localStorage.getItem('visitedLinks'));
      all.push(this.result.id);
      localStorage.setItem('visitedLinks', JSON.stringify(all));
    } else {
      let all: number[] = [];
      all.push(this.result.id);
      localStorage.setItem('visitedLinks', JSON.stringify(all));
    }
    this.visited = true;
    this.jobService.getCv(this.result.cv.path).subscribe((res) => {
      const fileURL = URL.createObjectURL(res);
      window.open(fileURL, '_blank');
    });;
  }
}
