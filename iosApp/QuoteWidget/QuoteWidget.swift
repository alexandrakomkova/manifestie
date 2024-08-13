//
//  QuoteWidget.swift
//  QuoteWidget
//
//  Created by Юзер on 13.08.24.
//  Copyright © 2024 orgName. All rights reserved.
//

import WidgetKit
import SwiftUI
import shared

struct Provider: TimelineProvider {
    
//    var timelineCancellable: AnyCancellable?
//    
//    private var entryPublisher: AnyPublisher<BikeShareStationEntry, Never> {
//        let future = Future<BikeShareStationEntry, Never> { promise in
//                    let repository =  CityBikesRepository()
//
//                    repository.fetchBikeShareInfo(network: "galway") { data, error in
//                        if let stationList = data {
//                            promise(.success(BikeShareStationEntry(date: Date(), station: stationList[0])))
//                        }
//                        if let errorReal = error {
//                            print(errorReal)
//                        }
//                    }
//                }
//        
//        return AnyPublisher(future)
//    }
    
    init() {
            KoinHelperKt.doInitKoinIOS()
        }
    
    func placeholder(in context: Context) -> SimpleEntry {
        SimpleEntry(date: Date(), quote: "blah blah")
    }
    
    func getSnapshot(in context: Context, completion: @escaping (SimpleEntry) -> ()) {
        let entry = SimpleEntry(date: Date(), quote: "blah blah")
        completion(entry)
    }
 
//    func getTimeline(in context: Context, completion: @escaping (Timeline<Entry>) -> ()) {
//            timelineCancellable = entryPublisher
//                .map { Timeline(entries: [$0], policy: .atEnd) }
//                .receive(on: DispatchQueue.main)
//                .sink(receiveValue: completion)
//        }
    
    func getTimeline(in context: Context, completion: @escaping (Timeline<SimpleEntry>) -> Void) {
            let currentDate = Date()
            var entries : [SimpleEntry] = []
        
           
            
            for minuteOffset in 0 ..< 60 {
                let entryDate = Calendar.current.date(byAdding: .minute, value: minuteOffset, to: currentDate)!
                let quote = "Never give up"
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
