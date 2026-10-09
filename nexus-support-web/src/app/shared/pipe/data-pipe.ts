import { Pipe, PipeTransform } from '@angular/core';

@Pipe({
  name: 'DataValue',
})
export class DataPipe implements PipeTransform {
  transform(value: unknown, ...args: unknown[]): unknown {
    if (args[0] === 'count') {
      const statuses = [...new Set()];
    }
    return;
  }

  private getWeekNumber = (date: Date | any) => {
    const firstDayOfYear: Date | any = new Date(date.getFullYear(), 0, 1);
    const daysPassed = Math.floor((date - firstDayOfYear) / (24 * 60 * 60 * 1000));
    const weekNumber = Math.ceil((daysPassed + firstDayOfYear.getDay() + 1) / 7);
    return `Year ${date.getFullYear()} - W${weekNumber.toString().padStart(2, '0')}`;
  };
}
