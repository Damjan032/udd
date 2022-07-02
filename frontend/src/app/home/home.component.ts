import { Component, OnInit } from '@angular/core';
import { Router } from '@angular/router';
import { Job } from '../core/models/response/job.model';
import { JobService } from '../core/services/job.service';

@Component({
  selector: 'app-home',
  templateUrl: './home.component.html',
  styleUrls: ['./home.component.scss']
})
export class HomeComponent implements OnInit {

  jobs: Job[] = [];

  constructor(
    private jobService: JobService,
    private router: Router
  ) { }

  ngOnInit(): void {
    this.getAllJobs();
  }

  private getAllJobs(): void {
    this.jobService.getAll().subscribe(jobs => {
      this.jobs = jobs.sort((a, b) => a.id - b.id);
    });
  }

  public apply(): void {
    this.router.navigate(['/apply'])
  }
}
