//
//  QuoteWidget.swift
//  QuoteWidget
//
//  Created by Юзер on 13.08.24.
//  Copyright © 2024 orgName. All rights reserved.
//

import WidgetKit
import SwiftUI
//import shared
import Combine

struct Tips {
    // 2.
    var tipsList: [String] = [
          "It is not a sprint, it is a marathon. One step at a time",
          "Success is the progressive realization of a worthy goal",
          "People with goals succeed because they know where they’re going",
          "All you need is the plan, the roadmap, and the courage to press on to your destination",
          "The opposite of courage in our society is not cowardice... it is conformity",
          "Whenever we’re afraid, it’s because we don’t know enough. If we understood enough, we would never be afraid",
          "The past does not equal the future",
          "The path to success is to take massive, determined action",
          "It’s what you practice in private that you will be rewarded for in public",
          "Small progress is still progress",
          "Don't worry if you find flaws in your past creations, it's because you've evolved",
          "Starting is the most difficult step - but you can do it",
          "Don't forget to enjoy the journey",
          "It's not a mistake, it's a learning opportunity",
    ]
}

struct Provider: TimelineProvider {
    

//    init() {
//            KoinHelperKt.doInitKoinIOS()
//        }
    
    func placeholder(in context: Context) -> SimpleEntry {
        SimpleEntry(date: Date(), quote: "Never give up!!")
    }
    
    func getSnapshot(in context: Context, completion: @escaping (SimpleEntry) -> ()) {
        let entry = SimpleEntry(date: Date(), quote: "The strength is in our scars")
        completion(entry)
    }
 

    private let waterTips = Tips()
    
    func getTimeline(in context: Context, completion: @escaping (Timeline<SimpleEntry>) -> Void) {
            let currentDate = Date()
            var entries : [SimpleEntry] = []
    
        
            for minuteOffset in 0 ..< 60 {
                let entryDate = Calendar.current.date(byAdding: .minute, value: minuteOffset, to: currentDate)!
//                var quote = "Future is gonna be okay"
//                DataStoreHelper().quoteCommonFlow.watch { it in
//                    // quote = it else { return }
//                    let q = (it as NSString?) //as String
//                    quote = q as! String
//                    
//                    print(quote)
//                }
                
                let quote = waterTips.tipsList[Int.random(in: 0...waterTips.tipsList.count-1)]
                let entry = SimpleEntry(date: entryDate, quote: quote)
                entries.append(entry)
            }
            
            let timeline = Timeline(entries: entries, policy: .atEnd)
            
            completion(timeline)
        }
}


struct SimpleEntry: TimelineEntry {
    let date: Date
    let quote: String
}

struct QuoteWidgetEntryView : View {
    var entry: Provider.Entry

    var body: some View {
    
        ZStack {
            Color(red: 0.4235, green: 0.3882, blue: 0.9059).ignoresSafeArea()
                    HStack {
                        Text("✨")
                            .unredacted()
                            .font(.system(size: 28))
                        Text(entry.quote)
                            .foregroundColor(Color(.white))
                            .font(.system(size: 18))
                    }.padding()
                }
        
    }
}


struct QuoteWidget: Widget {
    let kind: String = "QuoteWidget"

    var body: some WidgetConfiguration {
        StaticConfiguration(kind: kind, provider: Provider()) { entry in
            if #available(iOS 17.0, *) {
                QuoteWidgetEntryView(entry: entry)
                    .containerBackground(
                        Color("WidgetBackground"),
                        for: .widget)
            } else {
                QuoteWidgetEntryView(entry: entry)
                    .background(Color("WidgetBackground"))
            }
        }
        .configurationDisplayName("Manifestie Quote Widget")
        .description("Get your everyday inspo with this widget.")
    }
}

#Preview(as: .systemSmall) {
    QuoteWidget()
} timeline: {
    SimpleEntry(date: .now, quote: "meow meow")
}
