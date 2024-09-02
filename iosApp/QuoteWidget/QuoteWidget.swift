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
 

    
    func getTimeline(in context: Context, completion: @escaping (Timeline<SimpleEntry>) -> Void) {
            let currentDate = Date()
            var entries : [SimpleEntry] = []
        
            for minuteOffset in 0 ..< 60 {
                let entryDate = Calendar.current.date(byAdding: .minute, value: minuteOffset, to: currentDate)!
                let quote = "Future is gonna be okay"
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
        VStack {
            Text(entry.quote)
        }
    }
}


struct QuoteWidget: Widget {
    let kind: String = "QuoteWidget"

    var body: some WidgetConfiguration {
        StaticConfiguration(kind: kind, provider: Provider()) { entry in
            if #available(iOS 17.0, *) {
                QuoteWidgetEntryView(entry: entry)
                    .containerBackground(.fill.tertiary, for: .widget)
            } else {
                QuoteWidgetEntryView(entry: entry)
                    .padding()
                    .background()
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
