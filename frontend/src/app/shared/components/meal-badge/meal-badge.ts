import { Component, Input } from '@angular/core';
import { CommonModule } from '@angular/common';
import { MealType } from '../../../core/models/meal.model';

@Component({
  selector: 'app-meal-badge',
  imports: [CommonModule],
  template: `<span class="badge" [class]="mealType.toLowerCase()">{{ mealType }}</span>`,
  styleUrl: './meal-badge.scss',
})
export class MealBadge {
  @Input() mealType: MealType = 'BREAKFAST';
}
