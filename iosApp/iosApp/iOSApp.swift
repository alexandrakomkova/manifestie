import SwiftUI
import shared
import FirebaseCore
import UserNotifications


class NotificationManager {
    static func requestNotificationPermissions() {
        UNUserNotificationCenter.current().requestAuthorization(options: [.alert, .sound, .badge]) { granted, error in
            if granted {
                print("Notification permission granted.")
            } else if let error = error {
                print(error.localizedDescription)
            }
        }
    }
    
    static func scheduleDailyNotification(title: String, message: String, hour: Int, minute: Int) {
            let content = UNMutableNotificationContent()
            content.title = title
            content.body = message
            content.sound = .default

            var dateComponents = DateComponents()
            dateComponents.hour = hour
            dateComponents.minute = minute
            let trigger = UNCalendarNotificationTrigger(dateMatching: dateComponents, repeats: true)

            let request = UNNotificationRequest(identifier: "dailyNotification", content: content, trigger: trigger)

            UNUserNotificationCenter.current().add(request) { error in
                if let error = error {
                    print("Error: \(error.localizedDescription)")
                }
            }
        }
}

class AppDelegate: NSObject, UIApplicationDelegate {
  func application(_ application: UIApplication,
                   didFinishLaunchingWithOptions launchOptions: [UIApplication.LaunchOptionsKey : Any]? = nil) -> Bool {
    FirebaseApp.configure()
      NotificationManager.requestNotificationPermissions()
      NotificationManager.scheduleDailyNotification(
        title: "Daily Reminder",
        message: "Don't forget to check your tasks!",
        hour: 10,
        minute: 0
      )


    return true
  }
}


@main
struct iOSApp: App {
    @UIApplicationDelegateAdaptor(AppDelegate.self) var delegate
    
    init() {
        KoinHelperKt.doInitKoinIOS()
    }
    

	var body: some Scene {
		WindowGroup {
			ContentView()
		}
	}
}
