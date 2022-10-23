package com.polar.industries.teskotlin.helpers

import java.net.MalformedURLException
import java.net.URISyntaxException
import java.net.URL
import java.util.regex.Pattern

class StringHelper {
    private val EXREGEMAIL = "([a-z,0-9]+((\\.|_|-)[a-z0-9]+)*)@([a-z,0-9]+(\\.[a-z0-9]+)*)\\.([a-z]{2,})(\\.([a-z]{2}))?"

    public fun isEmpty(string: String): Boolean {
        return string.isEmpty()
    }

    public fun isEqual(string1: String, string2: String): Boolean {
        return string1 == string2
    }

    public fun isGreaterThanOrEqual(lengthString: Int, number: Int): Boolean {
        return lengthString >= number
    }

    public fun isEmail(email: String?): Boolean {
        return Pattern.matches(EXREGEMAIL, email)
    }

    public fun isNotEmptyCredentials(email: String?, password: String): Boolean {
        return isEmail(email) && !password.isEmpty()
    }

    public fun loginHelper(email: String, password: String): Int {
        if (isEmail(email) && !password.isEmpty()) {
            return 1 //Todo correcto
        } else {
            if (email.isEmpty() && !password.isEmpty()) {
                return 2 //Email vacío
            } else if (!isEmail(email) && !password.isEmpty()) {
                return 3 //Email invalido
            } else if (isEmail(email) && password.isEmpty()) {
                return 4 //Password vacío
            } else if (email.isEmpty() && password.isEmpty()) {
                return 5 //Email vacío y pasword vacío
            }
        }
        return 0
    }

    public fun validateURL(URL: String?): Boolean {
        return try {
            URL(URL).toURI()
            true
        } catch (e: URISyntaxException) {
            println(e)
            false
        } catch (ex: MalformedURLException) {
            println(ex)
            false
        }
    }
}