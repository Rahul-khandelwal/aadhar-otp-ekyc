package com.rahul.aadhar.controller

import com.rahul.aadhar.service.AuthService
import com.rahul.aadhar.utils.Verhoeff
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("auth")
class AuthController {

    @Autowired
    private lateinit var authService: AuthService

    @PostMapping(value = ["/request"])
    fun requestOtp(@RequestBody data: Map<String, Any>) : String {
        if (data["otp"] !is Int) {
            return "Invalid OTP provided"
        }

        if (!Verhoeff.validateVerhoeff(data["uid"] as String)) {
            return "Invalid Aadhar Number"
        }

        return authService.authenticate(data["uid"] as String, data["otp"].toString())
    }
}