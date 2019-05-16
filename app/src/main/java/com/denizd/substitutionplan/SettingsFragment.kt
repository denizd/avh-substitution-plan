package com.denizd.substitutionplan

import android.content.ActivityNotFoundException
import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.preference.PreferenceManager
import android.text.Editable
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.View
import android.widget.*
import androidx.appcompat.app.AlertDialog
import androidx.browser.customtabs.CustomTabsIntent
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import com.google.android.material.button.MaterialButton
import com.google.android.material.textfield.TextInputEditText
import com.jaredrummler.android.device.DeviceName
import java.lang.NullPointerException
import java.text.DecimalFormat
import java.util.*

class SettingsFragment : Fragment(R.layout.content_settings), View.OnClickListener {

    private lateinit var name: String
    private lateinit var model: String

    private lateinit var mContext: Context
    private lateinit var prefs: SharedPreferences
    private lateinit var edit: SharedPreferences.Editor
    private val builder = CustomTabsIntent.Builder()
    private val customTabsIntent = builder.build() as CustomTabsIntent
    private var cs: Int = 7

    override fun onAttach(context: Context) {
        super.onAttach(context)
        mContext = context
        prefs = PreferenceManager.getDefaultSharedPreferences(mContext)
        edit = prefs.edit()
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        builder.setToolbarColor(resources.getColor(R.color.white))

        val txtName = view.findViewById<TextInputEditText>(R.id.txtName)
        val txtClasses = view.findViewById<TextInputEditText>(R.id.txtClasses)
        val txtCourses = view.findViewById<TextInputEditText>(R.id.txtCourses)

        val switchDisableGreeting = view.findViewById<Switch>(R.id.switchDisableGreeting)
        val switchDark = view.findViewById<Switch>(R.id.switchDark)
        val switchHideInfo = view.findViewById<Switch>(R.id.switchHideInfo)
        val switchNotifications = view.findViewById<Switch>(R.id.switchNotifications)
        val switchDefaultPlan = view.findViewById<Switch>(R.id.switchDefaultPlan)
        val switchOpenInfo = view.findViewById<Switch>(R.id.switchOpenInfo)
        val switchAutoRefresh = view.findViewById<Switch>(R.id.switchAutoRefresh)

        val versionNumber = view.findViewById<TextView>(R.id.txtVersionTwo)
        val hiddenBtn = view.findViewById<LinearLayout>(R.id.btnHiddenENP)

        val helpCourses = view.findViewById<ImageButton>(R.id.chipHelpCourses)
        val helpClasses = view.findViewById<ImageButton>(R.id.chipHelpClasses)
        val btnCustomiseColours = view.findViewById<LinearLayout>(R.id.btnCustomiseColours)
        val btnWebsite = view.findViewById<LinearLayout>(R.id.btnWebsite)
        val btnLicences = view.findViewById<LinearLayout>(R.id.btnLicences)
        val btnTerms = view.findViewById<LinearLayout>(R.id.btnTerms)
        val btnPrivacy = view.findViewById<LinearLayout>(R.id.btnPrivacyP)
        val btnVersion = view.findViewById<LinearLayout>(R.id.btnVersion)

        switchDisableGreeting.setOnClickListener(this)
        switchDark.setOnClickListener(this)
        switchHideInfo.setOnClickListener(this)
        switchNotifications.setOnClickListener(this)
        switchDefaultPlan.setOnClickListener(this)
        switchOpenInfo.setOnClickListener(this)
        switchAutoRefresh.setOnClickListener(this)
        helpCourses.setOnClickListener(this)
        helpClasses.setOnClickListener(this)
        btnCustomiseColours.setOnClickListener(this)
        btnWebsite.setOnClickListener(this)
        btnLicences.setOnClickListener(this)
        btnTerms.setOnClickListener(this)
        btnPrivacy.setOnClickListener(this)
        btnVersion.setOnClickListener(this)

        switchDisableGreeting.isChecked = prefs.getBoolean("greeting", true)
        switchDark.isChecked = when (prefs.getInt("themeInt", 0)) {
            1 -> true
            else -> false
        }
        switchHideInfo.isChecked = prefs.getBoolean("showinfotab", true)
        switchNotifications.isChecked = prefs.getBoolean("notif", false)
        switchDefaultPlan.isChecked = prefs.getBoolean("defaultPersonalised", false)
        switchOpenInfo.isChecked = prefs.getBoolean("openInfo", false)
        switchAutoRefresh.isChecked = prefs.getBoolean("autoRefresh", false)

        versionNumber.text = BuildConfig.VERSION_NAME

        txtName.setText(prefs.getString("username", ""))
        txtName.addTextChangedListener(object: TextWatcher {
            override fun afterTextChanged(s: Editable) {}
            override fun beforeTextChanged(s: CharSequence, start: Int, count: Int, after: Int) {}
            override fun onTextChanged(s: CharSequence, start: Int, before: Int, count: Int) {
                edit.putString("username", txtName.text.toString()).apply()
            }
        })

        txtClasses.setText(prefs.getString("classes", ""))
        txtClasses.addTextChangedListener(object: TextWatcher {
            override fun afterTextChanged(s: Editable) {}
            override fun beforeTextChanged(s: CharSequence, start: Int, count: Int, after: Int) {}
            override fun onTextChanged(s: CharSequence, start: Int, before: Int, count: Int) {
                edit.putString("classes", txtName.text.toString()).apply()
            }
        })

        txtCourses.setText(prefs.getString("courses", ""))
        txtCourses.addTextChangedListener(object: TextWatcher {
            override fun afterTextChanged(s: Editable) {}
            override fun beforeTextChanged(s: CharSequence, start: Int, count: Int, after: Int) {}
            override fun onTextChanged(s: CharSequence, start: Int, before: Int, count: Int) {
                edit.putString("courses", txtName.text.toString()).apply()
            }
        })

        hiddenBtn.setOnClickListener {
            Toast.makeText(mContext, getString(R.string.bestclass), Toast.LENGTH_LONG).show()
        }

        hiddenBtn.setOnLongClickListener {
            debugMenu()
        }
    }

    override fun onClick(v: View?) {
        when (v?.id) {
            R.id.chipHelpCourses -> createDialog(getString(R.string.helpCoursesTitle), getString(R.string.helpCourses))
            R.id.chipHelpClasses -> createDialog(getString(R.string.helpClassesTitle), getString(R.string.helpClasses))
            R.id.switchDisableGreeting -> {
                val switchDisableGreeting = v.findViewById<Switch>(R.id.switchDisableGreeting)
                if (switchDisableGreeting.isChecked) edit.putBoolean("greeting", true) else edit.putBoolean("greeting", false)
            }
            R.id.switchDark -> {
                val switchDark = v.findViewById<Switch>(R.id.switchDark)
                if (switchDark.isChecked) edit.putInt("themeInt", 1) else edit.putInt("themeInt", 0)
            }
            R.id.switchHideInfo -> {
                val switchHideInfo = v.findViewById<Switch>(R.id.switchHideInfo)
                if (switchHideInfo.isChecked) edit.putBoolean("showinfotab", true) else edit.putBoolean("showinfotab", false)
            }
            R.id.btnCustomiseColours -> createColourDialog()
            R.id.switchNotifications -> {
                val switchNotifications = v.findViewById<Switch>(R.id.switchNotifications)
                if (switchNotifications.isChecked) edit.putBoolean("notif", true) else edit.putBoolean("notif", false)
            }
            R.id.switchDefaultPlan -> {
                val switchDefaultPlan = v.findViewById<Switch>(R.id.switchDefaultPlan)
                if (switchDefaultPlan.isChecked) edit.putBoolean("defaultPersonalised", true) else edit.putBoolean("defaultPersonalised", false)
            }
            R.id.switchOpenInfo -> {
                val switchOpenInfo = v.findViewById<Switch>(R.id.switchOpenInfo)
                if (switchOpenInfo.isChecked) edit.putBoolean("openInfo", true) else edit.putBoolean("openInfo", false)
            }
            R.id.switchAutoRefresh -> {
                val switchAutoRefresh = v.findViewById<Switch>(R.id.switchAutoRefresh)
                if (switchAutoRefresh.isChecked) edit.putBoolean("autoRefresh", true) else edit.putBoolean("autoRefresh", false)
            }
            R.id.btnWebsite -> {
                try {
                    customTabsIntent.launchUrl(mContext, Uri.parse("http://307.joomla.schule.bremen.de"))
                } catch (e: ActivityNotFoundException) {
                    Toast.makeText(mContext, getString(R.string.chromecompatible), Toast.LENGTH_LONG).show()
                }
            }
            R.id.btnLicences -> {
                createDialog("Licences", "Libraries:\n • Android Device Names © 2015 Jared Rummler, licensed under the Apache Licence, Version 2.0" +
                        "\n • EasyPreferences © 2018 Mukesh Solanki, licensed under the MIT Licence" +
                        "\n • jsoup HTML parser © 2009-2018 Jonathan Hedley, licensed under the open source MIT Licence" +
                        "\n\nFont:\n • Manrope © 2018-2019 Michael Sharanda, licensed under the SIL Open Font Licence 1.1" +
                        "\n\nIcons:\n • bqlqn\n • fjstudio\n • Freepik\n • Smashicons\n • © 2013-2019 Freepik Company S.L., licensed under Creative Commons BY 3.0" +
                        "\n\nMarketing & Publishing:\n • Leon Becker\n • Alex Lick\n • Batuhan Özcan\n • Erich Kerkesner")
            }
            R.id.btnTerms -> {
                createDialog("Terms & Conditions", "These terms automatically apply to anyone using this app - therefore, please make sure to read them carefully before using the app. Copying or modifying parts of the app or the entire app, as well as creating derivative versions, is prohibited without permission. This app is intellectual property of the developer.\n" +
                        "\nThe developer reserves the right to make changes to the app at any given time and for any reason. The user will be informed of any changes made accordingly.\n" +
                        "\nTampering with the end device, in the form of rooting or otherwise modifying the operating system may result in malfunction of the software. In this case, the developer does not provide support, as the user is responsible for keeping the device free of software that may compromise its security.\n" +
                        "\nFull functionality of the app relies on a steady internet connection. The developer does not take responsibility for malfunction of these services, nor for any errors caused by the end device. The user is responsible for ensuring that their device is fully updated and fully functioning.\n" +
                        "\nAs the app relies on third-parties, information provided in-app may be delayed or incorrect at times. The developer accepts no liability for any information mistakenly provided in the app.\n" +
                        "\nThe developer may wish to update the app at any point. Should such action arise, then the user is advised to update the app to ensure proper functionality. However, the developer does not promise that the app will be continuously updated, and may stop providing the app at any point. Unless told otherwise, upon any termination, (a) the rights and licenses granted to the user in these terms will end; (b) the user must stop using the app, and (if needed) delete it from their device.\n" +
                        "\nChanges to these Terms & Conditions\n" +
                        "\nThe developer may update these Terms & Conditions from time to time. Thus, the user is advised to review this page periodically for any changes. Any changes are effective immediately after they are posted on this page.\n" +
                        "\nContact Us\n" +
                        "\nIn case of questions or suggestions regarding these Terms & Conditions, the user is advised to contact the developer of this app. Contact information is provided through the Play Store.\n" +
                        "\n---\n" +
                        "\nThese Terms & Conditions were modified upon the template provided by App Privacy Policy Generator. This service is in no way affiliated with AvH Plan or the developer.")
            }
            R.id.btnPrivacyP -> {
                createDialog("Privacy Policy", "The app \"Alexander-von-Humboldt-Plan\", hereby abbreviated to AvH Plan, is provided as a free service by the developer for use as is.\n" +
                        "\nThis page is used to inform visitors and users regarding policies with the collection, use, and disclosure of personal information, if they choose to make use of the service.\n" +
                        "\nAvH Plan does not collect, store, share, or in any other way use personal information retrieved from its users. Any information asked for within the app is only used to improve the user experience and as such, is stored locally and cannot be accessed by third-parties or be used to identify the user.\n" +
                        "\nInformation Collection and Use\n" +
                        "\nAt this moment, AvH Plan does not require personally identifiable information for its core functionality. Certain features do require entering information that may be personally identifiable, however, this data will be retained on the user's device and is therefore not accessible outside the app\n" +
                        "\nLog Data\n" +
                        "\nIn the case of an error in the app, device-specific data may be collected and stored on your phone. This data is called Log Data. This Log Data may include information, such as your device's Internet Protocol (“IP”) address, device name, operating system version, the configuration of the app when utilising the service, the time and date when the last usage occured, and other statistics. This data can not be used to personally identify any user.\n" +
                        "\nCookies\n" +
                        "\nCookies are files with small amounts of data that are commonly used as anonymous unique identifiers on the web. AvH Plan strives to not make use of cookies for its functionality, however, as its functionality requires access to third-party-websites, cookies may be unintentionally collected by the device's web browser. AvH Plan does not make use of these cookies in any way.\n" +
                        "\nService Providers\n" +
                        "\nNo third-parties are employed for providing any functionality or services of the AvH Plan-app and as such, all functionality is accessible without directly sharing personal data with third-parties.\n" +
                        "\nSecurity\n" +
                        "\nWhile absolute security cannot be ensured due to the internet functionality the app employs, no information is shared over any transmission methods.\n" +
                        "\nLinks to Other Sites\n" +
                        "\nThis Service may contain links to other sites, which are not operated by the developer of AvH Plan. The user is advised to review the privacy policy of any webpage they may access through the app. No responsibility for the content of these third-party websites is provided by the app developer.\n" +
                        "\nChildren’s Privacy\n" +
                        "\nThe service does not address anyone under the age of 13. As the app does not collect any information from its users, no data is collected from users that may be under the age of 13.\n" +
                        "\nChanges to This Privacy Policy\n" +
                        "\nThis privacy policy may be updated as the AvH Plan app is updated with new features. As such, the user is advised to review this page periodically for any changes. Any changes that may be made are effective immediately upon publishing.\n" +
                        "\nContact Us\n" +
                        "\nIn case of questions or suggestions regarding this privacy policy, the user is advised to contact the developer of this app. Contact information is provided through the Play Store.\n" +
                        "\n---\n" +
                        "\nThis privacy policy was modified upon the template provided by privacypolicytemplate.net and App Privacy Policy Generator. These services are in no way affiliated with AvH Plan or the developer.")
            }
            R.id.btnVersion -> {
                if (cs > 0) {
                    cs--
                } else {
                    try {
                        cs = 7
                        customTabsIntent.launchUrl(mContext, Uri.parse("http://www.flussufer.de/gerd/person.htm"))
                        Toast.makeText(mContext, getString(R.string.donttell), Toast.LENGTH_LONG).show()
                    } catch (e: ActivityNotFoundException) {
                        Toast.makeText(mContext, getString(R.string.chromecompatible), Toast.LENGTH_LONG).show()
                    }
                }
            }
        }
        edit.apply()
    }

    private fun createDialog(title: String, text: String) {
        val alertDialog = if (prefs.getInt("themeInt", 0) == 1) {
            AlertDialog.Builder(mContext, R.style.AlertDialogCustomDark)
        } else {
            AlertDialog.Builder(mContext, R.style.AlertDialogCustomLight)
        }
        val dialogView = LayoutInflater.from(mContext).inflate(R.layout.simple_dialog, null)
        val dialogTitle = dialogView.findViewById<TextView>(R.id.textviewtitle)
        val dialogText = dialogView.findViewById<TextView>(R.id.dialogtext)
        dialogTitle.text = title
        dialogText.text = text
        alertDialog.setView(dialogView).show()
    }

    private fun createColourDialog() {

        val coursesNoLang = arrayOf("German", "English", "French", "Spanish", "Latin", "Turkish", "Chinese", "Arts", "Music", "Theatre", "Geography", "History", "Politics", "Philosophy", "Religion", "Maths", "Biology", "Chemistry", "Physics", "CompSci", "PhysEd", "GLL", "WAT", "Forder", "WP")
        val courses = arrayOf(getString(R.string.courseDeu), getString(R.string.courseEng), getString(R.string.courseFra), getString(R.string.courseSpa), getString(R.string.courseLat), getString(R.string.courseTue), getString(R.string.courseChi), getString(R.string.courseKun), getString(R.string.courseMus), getString(R.string.courseDar), getString(R.string.courseGeg), getString(R.string.courseGes), getString(R.string.coursePol), getString(R.string.coursePhi), getString(R.string.courseRel), getString(R.string.courseMat), getString(R.string.courseBio), getString(R.string.courseChe), getString(R.string.coursePhy), getString(R.string.courseInf), getString(R.string.courseSpo), getString(R.string.courseGll), getString(R.string.courseWat), getString(R.string.courseFor), getString(R.string.courseWp))
        val coursesIcons = intArrayOf(R.drawable.ic_german, R.drawable.ic_english, R.drawable.ic_french, R.drawable.ic_spanish, R.drawable.ic_latin, R.drawable.ic_turkish, R.drawable.ic_chinese, R.drawable.ic_arts, R.drawable.ic_music, R.drawable.ic_drama, R.drawable.ic_geography, R.drawable.ic_history, R.drawable.ic_politics, R.drawable.ic_philosophy, R.drawable.ic_religion, R.drawable.ic_maths, R.drawable.ic_biology, R.drawable.ic_chemistry, R.drawable.ic_physics, R.drawable.ic_compsci, R.drawable.ic_pe, R.drawable.ic_gll, R.drawable.ic_wat, R.drawable.ic_help, R.drawable.ic_pencil)

        val colourCustomiserBuilder = when (prefs.getInt("themeInt", 0)) {
            1 -> AlertDialog.Builder(mContext, R.style.AlertDialogCustomDark)
            else -> AlertDialog.Builder(mContext, R.style.AlertDialogCustomLight)
        }

        val dialogView = LayoutInflater.from(mContext).inflate(R.layout.empty_dialog, null)
        val titleText = dialogView.findViewById<TextView>(R.id.empty_textviewtitle)
        titleText.text = getString(R.string.customisecolor1)
        val emptyLayout = dialogView.findViewById<LinearLayout>(R.id.empty_linearlayout)

        for (i in 0 until coursesNoLang.size) {
            val item = LayoutInflater.from(mContext).inflate(R.layout.list_item, null)
            val itemText = item.findViewById<TextView>(R.id.item_text)
            val itemImage = item.findViewById<ImageView>(R.id.item_image)
            val layout = item.findViewById<LinearLayout>(R.id.item_layout)

            itemText.text = courses[i]
            itemImage.setImageDrawable(resources.getDrawable(coursesIcons[i]))

            val courseNoLangTxt = coursesNoLang[i]
            val courseTxt = courses[i]

            if (prefs.getInt("col" + coursesNoLang[i], 0) != 0) {
                itemText.setTextColor(ContextCompat.getColor(mContext, prefs.getInt("col$courseNoLangTxt", 0)))
                itemImage.drawable.setTint(ContextCompat.getColor(mContext, prefs.getInt("col$courseNoLangTxt", 0)))
            }

            layout.setOnClickListener {
                val colourPickerBuilder = when (prefs.getInt("themeInt", 0)) {
                    1 -> AlertDialog.Builder(mContext, R.style.AlertDialogCustomDark)
                    else -> AlertDialog.Builder(mContext, R.style.AlertDialogCustomLight)
                }
                val pickerDialogView = LayoutInflater.from(mContext).inflate(R.layout.empty_dialog, null)
                val pickerTitleText = pickerDialogView.findViewById<TextView>(R.id.empty_textviewtitle)
                val pickerLayout = pickerDialogView.findViewById<LinearLayout>(R.id.empty_linearlayout)
                pickerTitleText.text = courseTxt

                val picker = LayoutInflater.from(mContext).inflate(R.layout.colour_picker, null)
                pickerLayout.addView(picker)
                colourPickerBuilder.setView(pickerDialogView)
                val colourPickerDialog: AlertDialog = colourPickerBuilder.create()

                val buttons = arrayOf<MaterialButton>(picker.findViewById(R.id.colourRed), picker.findViewById(R.id.colourPink), picker.findViewById(R.id.colourPurple), picker.findViewById(R.id.colourDeepPurple), picker.findViewById(R.id.colourLavender), picker.findViewById(R.id.colourIndigo), picker.findViewById(R.id.colourBlue), picker.findViewById(R.id.colourLightBlue), picker.findViewById(R.id.colourTeal), picker.findViewById(R.id.colourGreen), picker.findViewById(R.id.colourYellow), picker.findViewById(R.id.colourOrange), picker.findViewById(R.id.colourNeonRed), picker.findViewById(R.id.colourNeonPink), picker.findViewById(R.id.colourNeonPurple), picker.findViewById(R.id.colourNeonDeepPurple), picker.findViewById(R.id.colourNeonBlue), picker.findViewById(R.id.colourNeonGreen), picker.findViewById(R.id.colourNeonYellow), picker.findViewById(R.id.colourNeonOrange), picker.findViewById(R.id.colourNone))
                val colours = intArrayOf(R.color.red, R.color.pink, R.color.purple, R.color.deeppurple, R.color.lavender, R.color.indigo, R.color.blue, R.color.lightblue, R.color.teal, R.color.green, R.color.yellow, R.color.orange, R.color.neonred, R.color.neonpink, R.color.neonpurple, R.color.neondeeppurple, R.color.neonblue, R.color.neongreen, R.color.neonyellow, R.color.neonorange, 0)

                for (i2 in 0 until buttons.size) {
                    buttons[i2].setOnClickListener {
                        edit.putInt("col$courseNoLangTxt", colours[i2]).apply()
                        colourPickerDialog.dismiss()
                    }
                }

                colourPickerDialog.show()
            }
            emptyLayout.addView(item)
        }

        val button = LayoutInflater.from(mContext).inflate(R.layout.forever_alone_button, null)
        val buttonClearAll = button.findViewById<MaterialButton>(R.id.buttonclearallcolours)
        emptyLayout.addView(button)
        colourCustomiserBuilder.setView(dialogView)
        val colourCustomiserDialog = colourCustomiserBuilder.create()

        buttonClearAll.setOnClickListener {
            for (course in coursesNoLang) {
                edit.putInt("col$course", 0)
            }
            edit.apply()
            Toast.makeText(mContext, getString(R.string.allcolourscleared),
                    Toast.LENGTH_LONG).show()
            colourCustomiserDialog.cancel()
        }

        colourCustomiserDialog.show()

    }

    private fun debugMenu(): Boolean {

        DeviceName.with(mContext).request { deviceInfo, _ ->
            name = deviceInfo.marketName
            model = deviceInfo.model
        }

        val alertDialog = when (prefs.getInt("themeInt", 0)) {
            1 -> AlertDialog.Builder(mContext, R.style.AlertDialogCustomDark)
            else -> AlertDialog.Builder(mContext, R.style.AlertDialogCustomLight)
        }
        val dialogView = LayoutInflater.from(mContext).inflate(R.layout.edittext_dialog, null)
        val dialogEditText = dialogView.findViewById<EditText>(R.id.dialog_edittext)
        val dialogButton = dialogView.findViewById<Button>(R.id.dialog_button)

        dialogView.findViewById<TextView>(R.id.textviewtitle).text = getString(R.string.experimentalmenu)
        dialogView.findViewById<TextView>(R.id.dialogtext).text = getString(R.string.fortest)
        dialogButton.setOnClickListener {
            when (dialogEditText.text.toString()) {
                "@DIAGNOSTICS" -> {
                    val alertDialogDev = when (prefs.getInt("themeInt", 0)) {
                        1 -> AlertDialog.Builder(mContext, R.style.AlertDialogCustomDark)
                        else -> AlertDialog.Builder(mContext, R.style.AlertDialogCustomLight)
                    }
                    val devDialogView = LayoutInflater.from(mContext).inflate(R.layout.diagnostics_dialog, null)
                    val devDialogText = devDialogView.findViewById<TextView>(R.id.dialogtext)
                    val resetLaunchBtn = devDialogView.findViewById<Button>(R.id.btnResetLaunch)
                    val resetNotifBtn = devDialogView.findViewById<Button>(R.id.btnResetNotif)

                    devDialogView.findViewById<TextView>(R.id.textviewtitle).text = getString(R.string.diagnosticsmenu)
                    devDialogText.text = getDiagnosticsText(prefs)

                    resetLaunchBtn.setOnClickListener {
                        edit.putInt("launchDev", 0).apply()
                        devDialogText.text = getDiagnosticsText(prefs)
                    }

                    resetNotifBtn.setOnClickListener {
                        edit.putInt("notificationTestNumberDev", 0).apply()
                        devDialogText.text = getDiagnosticsText(prefs)
                    }

                    alertDialogDev.setView(devDialogView)
                    alertDialogDev.show()
                }
                "2018-04-20" -> {
                    try {
                        val intent = Intent(Intent.ACTION_VIEW, Uri.parse("https://www.youtube.com/watch?v=u8tdT5pAE34"))
                        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK).setPackage("com.google.android.youtube")
                        startActivity(intent)
                    } catch (e: ActivityNotFoundException) {
                        Toast.makeText(mContext, R.string.noyoutube, Toast.LENGTH_LONG).show()
                    }
                }
                "Q1 Matchmaker" -> {
                    val gen = Random()
                    val alertDialogDate = when (prefs.getInt("themeInt", 0)) {
                        1 -> AlertDialog.Builder(mContext, R.style.AlertDialogCustomDark)
                        else -> AlertDialog.Builder(mContext, R.style.AlertDialogCustomLight)
                    }
                    val dateDialogView = LayoutInflater.from(mContext).inflate(R.layout.dating_dialog, null)
                    val datingBtn = dateDialogView.findViewById<Button>(R.id.dating_btn)
                    val boyTxt = dateDialogView.findViewById<TextInputEditText>(R.id.dating_txt1)
                    val girlTxt = dateDialogView.findViewById<TextInputEditText>(R.id.dating_txt2)
                    val boyCb = dateDialogView.findViewById<CheckBox>(R.id.cb1)
                    val girlCb = dateDialogView.findViewById<CheckBox>(R.id.cb2)
                    val percentage = dateDialogView.findViewById<TextView>(R.id.dating_txtp)

                    dateDialogView.findViewById<TextView>(R.id.textviewtitle).text = getString(R.string.dating)

                    datingBtn.setOnClickListener {
                        try {
                            if (boyTxt.text.toString().isEmpty() || boyCb.isChecked) {
                                boyTxt.setText(DataGetter.boy[gen.nextInt(DataGetter.boy.size)])
                            }
                            if (girlTxt.text.toString().isEmpty() || girlCb.isChecked) {
                                girlTxt.setText(DataGetter.girl[gen.nextInt(DataGetter.girl.size)])
                            }

                            val boyP = boyTxt.text!!.length.toDouble()
                            val girlP = girlTxt.text!!.length.toDouble()

                            var perc = when {
                                girlP < boyP -> (girlP / boyP) * 100
                                else -> (boyP / girlP) * 100
                            }

                            val boyChar = boyTxt.text.toString()[boyTxt.text.toString().length - 1]
                            val girlChar = girlTxt.text.toString()[girlTxt.text.toString().length - 1]
                            val multiply = (Character.getNumericValue(boyChar) + Character.getNumericValue(girlChar)) / 1.2
                            if (perc == 0.0) {
                                perc += 30.0
                            }
                            val df = DecimalFormat("#.##")
                            percentage.text = when {
                                perc * (multiply / 40) > 100 -> "100.00%"
                                else -> df.format(perc * (multiply / 40)) + "%"
                            }

                        } catch (e: NullPointerException) {
                            Toast.makeText(mContext, getString(R.string.error), Toast.LENGTH_SHORT).show()
                        }
                    }
                    alertDialogDate.setView(dateDialogView)
                    alertDialogDate.show()
                }
                "@NOTIFICATION" -> {
                    edit.putString("time", "").apply()
                    Toast.makeText(mContext, "Notification time cleared", Toast.LENGTH_LONG).show()
                }
                else -> Toast.makeText(mContext, getString(R.string.nothinghappened), Toast.LENGTH_LONG).show()
            }
        }
        alertDialog.setView(dialogView);
        alertDialog.show();
        return true
    }

    private fun getDiagnosticsText(prefs: SharedPreferences): String {
        return "First Launch: " + prefs.getString("firstTimeDev", "") +
                "\n\nApp launched: " + prefs.getInt("launchDev", 0) +
                "\n\nNotification Service fired: " + prefs.getInt("notificationTestNumberDev", 0) +
                "\n\nDevice Name: " + name +
                "\n\nDevice Model: " + model +
                "\n\nAndroid Version: " + Build.VERSION.RELEASE
    }
}