package com.wonderland.trail.model

import java.sql.Date

case class TestData(id: Long,
                    firstName: String,
                    lastName: String,
                    nickName: String,
                    dateOfBirth: Date,
                    gender: String,
                    ssn: String
                   )
