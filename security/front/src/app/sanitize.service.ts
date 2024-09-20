import { Injectable } from '@angular/core';
import * as DOMPurify from 'dompurify';


@Injectable({
  providedIn: 'root'
})
export class SanitizeService {
  constructor() { }

  sanitizeInput(input: string): string {
    return DOMPurify.sanitize(input);
  }
}
