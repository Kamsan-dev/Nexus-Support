export const mockData = {
  summary: {
    totalTickets: 248,
    completedTickets: 96,
    inProgressTickets: 54,
    impededTickets: 12,
  },
  ticketsByType: [
    {
      type: 'BUG',
      count: 82,
    },
    {
      type: 'FEATURE',
      count: 64,
    },
    {
      type: 'SUPPORT',
      count: 58,
    },
    {
      type: 'TASK',
      count: 44,
    },
  ],
  ticketsByStatus: [
    {
      status: 'NEW',
      count: 28,
    },
    {
      status: 'IN_PROGRESS',
      count: 54,
    },
    {
      status: 'IN_REVIEW',
      count: 30,
    },
    {
      status: 'COMPLETED',
      count: 96,
    },
    {
      status: 'IMPEDED',
      count: 12,
    },
    {
      status: 'PENDING',
      count: 22,
    },
    {
      status: 'CLOSED',
      count: 6,
    },
  ],
  weeklyTrends: [
    {
      weekStart: '2026-08-24',
      weekLabel: 'Aug 24',
      created: 18,
      completed: 12,
    },
    {
      weekStart: '2026-08-31',
      weekLabel: 'Aug 31',
      created: 24,
      completed: 16,
    },
    {
      weekStart: '2026-09-07',
      weekLabel: 'Sep 07',
      created: 21,
      completed: 19,
    },
    {
      weekStart: '2026-09-14',
      weekLabel: 'Sep 14',
      created: 29,
      completed: 17,
    },
    {
      weekStart: '2026-09-21',
      weekLabel: 'Sep 21',
      created: 32,
      completed: 25,
    },
    {
      weekStart: '2026-09-28',
      weekLabel: 'Sep 28',
      created: 26,
      completed: 22,
    },
    {
      weekStart: '2026-10-05',
      weekLabel: 'Oct 05',
      created: 35,
      completed: 28,
    },
    {
      weekStart: '2026-10-12',
      weekLabel: 'Oct 12',
      created: 14,
      completed: 9,
    },
  ],
};
