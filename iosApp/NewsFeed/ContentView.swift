//
//  ContentView.swift
//  NewsFeed
//
//  Created by Martins on 4/15/21.
//

import SwiftUI
import shared

struct ContentView: View {
    
    @StateObject var viewModel = NewsFeedViewModel()
    
    var body: some View {
        NavigationView {
            listView()
                .navigationBarTitle("NewsFeed")
                .navigationBarItems(trailing:
                                        Button("Reload") {
                                            self.viewModel.getNewsFeeds(fetched: true)
                                        })
                .onDisappear(perform: {
                    viewModel.cancelJob()
                })
        }
    }
    
    private func listView() -> AnyView {
        switch viewModel.launches {
        case .loading:
            return AnyView(Text("Loading...").multilineTextAlignment(.center))
        case .result(let news):
            return AnyView(List(news) { news in
                NavigationLink(destination: LoadingView(viewModel: self.viewModel, content: {
                    WebView(url: news.url, viewModel: self.viewModel).onDisappear(perform: {
                        viewModel.webViewLoading = true
                    })
                })) {
                    NewsItemView(newsFeed: news)
                        .listRowInsets(EdgeInsets())
                }
            })
        case .error( _):
            return AnyView(Text("Error getting view").multilineTextAlignment(.center))
        }
    }
}

struct ContentView_Previews: PreviewProvider {
    static var previews: some View {
        ContentView()
    }
}

extension NewsFeed: Identifiable {}
