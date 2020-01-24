package com.rahul.aadhar.controller

import com.rahul.aadhar.utils.Verhoeff
import com.rahul.aadhar.service.OtpService
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("otp")
class OtpController {

    @Autowired
    private lateinit var otpService: OtpService

    @PostMapping(value = ["/request"])
    fun requestOtp(@RequestBody uid: String) : String {
        if (!Verhoeff.validateVerhoeff(uid)) {
            return "Invalid Aadhar Number"
        }

        return otpService.sendOtp(uid)
    }
}