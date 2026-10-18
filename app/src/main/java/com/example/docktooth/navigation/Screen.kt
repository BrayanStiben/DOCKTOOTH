package com.example.docktooth.navigation

sealed class Screen(val route: String) {
    object Splash : Screen("splash")
    object Login : Screen("login")
    object Register : Screen("register")
    object Dashboard : Screen("dashboard")
    object Agenda : Screen("agenda")
    object ClinicalHistory : Screen("clinical_history")
    object ClinicalRecordForm : Screen("clinical_record_form")
    object Billing : Screen("billing")
    object InvoiceView : Screen("invoice_view")
    object TimePass : Screen("time_pass")
}
