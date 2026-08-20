# Getting Started

### Reference Documentation
For further reference, please consider the following sections:

* [Official Apache Maven documentation](https://maven.apache.org/guides/index.html)
* [Spring Boot Maven Plugin Reference Guide](https://docs.spring.io/spring-boot/4.1.0/maven-plugin)
* [Create an OCI image](https://docs.spring.io/spring-boot/4.1.0/maven-plugin/build-image.html)
* [Spring Web](https://docs.spring.io/spring-boot/4.1.0/reference/web/servlet.html)
* [Spring Data JPA](https://docs.spring.io/spring-boot/4.1.0/reference/data/sql.html#data.sql.jpa-and-spring-data)

### Guides
The following guides illustrate how to use some features concretely:

* [Accessing data with MySQL](https://spring.io/guides/gs/accessing-data-mysql/)
* [Building a RESTful Web Service](https://spring.io/guides/gs/rest-service/)
* [Serving Web Content with Spring MVC](https://spring.io/guides/gs/serving-web-content/)
* [Building REST services with Spring](https://spring.io/guides/tutorials/rest/)
* [Accessing Data with JPA](https://spring.io/guides/gs/accessing-data-jpa/)

### Maven Parent overrides

Due to Maven's design, elements are inherited from the parent POM to the project POM.
While most of the inheritance is fine, it also inherits unwanted elements like `<license>` and `<developers>` from the parent.
To prevent this, the project POM contains empty overrides for these elements.
If you manually switch to a different parent and actually want the inheritance, you need to remove those overrides.

Post API:


http://localhost:8080/api/appointments     Post


{
"patientId": 1,
"appointmentDate": "2026-08-13",
"appointmentTime": "09:00",
"appointmentType": "In Clinic",
"reason": "Ear recheck",
"status": "Scheduled",
"notes": "Follow-up examination",
"doctorName": "Dr. Kumar"
}

http://localhost:8080/api/appointment    post

{
"patientId": 3,
"appointmentDate": "2026-08-13",
"appointmentTime": "09:00",
"appointmentType": "In Clinic",
"reason": "Ear recheck",
"status": "Scheduled",
"notes": "Follow-up examination",
"doctorName": "Dr. Kumar"
}



http://localhost:8080/api/owners  post


{
"fullName": "Rohan Sharma",
"phone": "9876543210",
"email": "rohan@example.com",
"address": "MG Road",
"city": "Bangalore",
"state": "Karnataka",
"pincode": "560001",
"emergencyContact": "9876500000",
"notes": "Regular customer"
}



http://localhost:8080/api/medical-records    post


{
"patientId": 3,
"visitDate": "2026-08-13",
"chiefComplaint": "Ear irritation",
"symptoms": "Head shaking and scratching",
"diagnosis": "Otitis externa",
"clinicalFindings": "Redness and mild discharge in right ear",
"treatment": "Ear cleaning and medication",
"weight": 28.5,
"temperature": 38.5,
"notes": "Follow-up required after 7 days",
"doctorName": "Dr. Kumar"
}


POST http://localhost:8080/api/prescriptions
Content-Type: application/json




{
"patientId": 1,
"medicalRecordId": 1,
"prescriptionDate": "2026-08-13",
"diagnosis": "Ear infection",
"instructions": "Give medicines after food",
"notes": "Follow up after 7 days",
"doctorName": "Dr. Kumar",
"items": [
{
"medicineId": 1,
"dosage": "500 mg",
"frequency": "Twice daily",
"duration": "7 days",
"route": "Oral",
"quantity": "14",
"instructions": "Give after food"
}
]
}




POST http://localhost:8080/api/medicines     post


{
"name": "Amoxicillin",
"category": "Antibiotic",
"manufacturer": "ABC Pharma",
"description": "Broad spectrum antibiotic",
"dosageForm": "Tablet",
"strength": "500 mg",
"unit": "Tablet",
"price": 12.50,
"stockQuantity": 100,
"reorderLevel": 20,
"status": "Active"
}



POST http://localhost:8080/api/vaccinations


{
"patientId": 3,
"vaccineName": "Rabies",
"vaccineType": "Core",
"manufacturer": "Zoetis",
"batchNumber": "RAB202608",
"vaccinationDate": "2026-08-12",
"nextDueDate": "2027-08-12",
"dosage": "1 ml",
"route": "Subcutaneous",
"administeredBy": "Dr. Kumar",
"status": "Completed",
"notes": "Annual rabies vaccination"
}



POST http://localhost:8080/api/follow-ups



{
"patientId": 3,
"appointmentId": 1,
"followUpDate": "2026-08-12",
"nextFollowUpDate": "2026-09-12",
"reason": "Post treatment follow-up",
"status": "Scheduled",
"symptoms": "No major symptoms",
"findings": "Recovery is good",
"treatment": "Continue prescribed medication",
"recommendations": "Regular diet and exercise",
"notes": "Owner advised to monitor recovery",
"doctorName": "Dr. Kumar",
"reminderSent": false
}



GET http://localhost:8080/api/invoices



POST http://localhost:8080/api/invoices
Content-Type: application/json



{
"patientId": 3,
"ownerId": 1,
"appointmentId": 1,
"invoiceDate": "2026-08-12",
"dueDate": "2026-08-20",
"subtotal": 1000,
"discount": 100,
"tax": 90,
"paidAmount": 500,
"paymentMethod": "UPI",
"transactionId": "TXN-10001",
"notes": "Veterinary consultation and treatment"
}


POST http://localhost:8080/api/notifications
Content-Type: application/json


{
"title": "Appointment Reminder",
"message": "Bruno has an appointment today at 10:30 AM.",
"type": "Appointment",
"userId": 1,
"patientId": 1,
"appointmentId": 1,
"priority": "High"
}




POST http://localhost:8080/api/sms
Content-Type: application/json



{
"phoneNumber": "9876543210",
"message": "Your pet appointment is scheduled for today.",
"type": "Appointment",
"provider": "Default"
}



http://localhost:8080/api/whatsapp   post

{
"phoneNumber": "9876543210",
"message": "Your pet appointment is scheduled for tomorrow.",
"type": "Appointment",
"provider": "Default"
}



http://localhost:8080/api/emails   post

{
"recipient": "test@example.com",
"subject": "Appointment Reminder",
"message": "Your pet appointment is scheduled for tomorrow.",
"type": "Appointment",
"provider": "Default"
}



http://localhost:8080/api/invoices   post


{
"ownerId": 1,
"patientId": 3,
"invoiceDate": "2026-08-12",
"dueDate": "2026-08-19",
"subtotal": 1500,
"tax": 180,
"discount": 100,
"paidAmount": 500,
"notes": "Consultation and medicines"
}



http://localhost:8080/api/payments    post

{
"invoiceId": 1,
"ownerId": 1,
"patientId": 1,
"amount": 500,
"paymentMethod": "UPI",
"transactionId": "TXN-20260812-001",
"referenceNumber": "UPI-REF-001",
"notes": "Partial invoice payment"
}