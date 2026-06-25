# Campus Notification System Design

## Stage 1: REST API Design

The notification system can have the following REST APIs.

### Create Notification

**POST** `/notifications`

This API is used to create a new notification for a student.

Request Example:

```json
{
  "studentId": "23211A66J6",
  "type": "Placement",
  "message": "Google Hiring Drive",
  "timestamp": "2026-06-25T10:30:00"
}
```

Response:

```json
{
  "id": "uuid",
  "message": "Notification created successfully"
}
```

Status Code: **201 Created**

---

### Get All Notifications

**GET** `/notifications`

This API returns all notifications available for a student.

Status Code: **200 OK**

---

### Get Notification by ID

**GET** `/notifications/{id}`

This API returns the details of a single notification.

Possible Status Codes:

* 200 OK
* 404 Not Found

---

### Mark Notification as Read

**PATCH** `/notifications/{id}/read`

This API changes the notification status from unread to read.

Possible Status Codes:

* 200 OK
* 404 Not Found

---

### Delete Notification

**DELETE** `/notifications/{id}`

This API removes a notification from the system.

Status Code:

* 204 No Content

---

# Stage 2: Database Design

The system can use two tables.

### Student Table

| Column     | Type    |
| ---------- | ------- |
| student_id | VARCHAR |
| name       | VARCHAR |
| email      | VARCHAR |

Primary Key:

`student_id`

---

### Notification Table

| Column          | Type      |
| --------------- | --------- |
| notification_id | UUID      |
| student_id      | VARCHAR   |
| type            | VARCHAR   |
| message         | TEXT      |
| is_read         | BOOLEAN   |
| created_at      | TIMESTAMP |

Primary Key:

`notification_id`

Foreign Key:

`student_id → Student.student_id`

Indexes:

* student_id
* is_read
* created_at

These indexes help in retrieving notifications quickly.

---

# Stage 3: SQL Optimization

Original Query

```sql
SELECT *
FROM notification
WHERE student_id = ?
AND is_read = false
ORDER BY created_at DESC;
```

When the notification table becomes very large, this query may become slow because it needs to search and sort many records.

To improve the performance, I would create the following composite index.

```sql
CREATE INDEX idx_notification
ON notification(student_id, is_read, created_at DESC);
```

This reduces the search time and improves the sorting performance.

---

# Stage 4: Performance Improvements

To improve the performance of the notification system, I would use the following techniques.

* Use Redis to cache frequently accessed notifications.
* Load notifications page by page instead of loading everything at once.
* Store the unread notification count separately.
* Compress API responses.
* Use WebSocket so that users receive notifications instantly without refreshing the page.

These changes help the application respond faster and reduce database load.

---

# Stage 5: Scalable Notification Processing

Instead of sending notifications directly from the application, I would use a message queue.

Flow:

```
User Action
      │
      ▼
Notification API
      │
      ▼
Message Queue
      │
      ▼
Notification Worker
      │
      ├── Save to Database
      ├── Send Push Notification
      └── Send Email
```

Using a queue makes the system more scalable because requests are processed in the background. It also allows retries if a notification fails to send and prevents the application from slowing down during heavy traffic.

---

# Stage 6: Priority Inbox

The priority inbox was implemented in Spring Boot.

The application first fetches all notifications from the API. After that, it sorts them based on the required priority.

Priority Order:

1. Placement
2. Result
3. Event

If two notifications have the same priority, the most recent notification is displayed first.

Finally, the application returns only the top 10 notifications through the following endpoint.

`GET /notifications/top10`
