//
//  ViewModel.swift
//  NewsFeed
//
//  Created by Martins on 4/20/21.
//

import Foundation
import shared


class NewsFeedViewModel: ObservableObject {
    
    enum LoadableLaunches {
        case loading
        case result([NewsFeed])
        case error(String)
    }
    
    @Published var launches = LoadableLaunches.loading
    @Published var webViewLoading = true
    private var nativeViewModel: IOSNewsFeedVM
    
    init() {
        nativeViewModel = IOSNewsFeedVM()
        getNewsFeeds(fetched: true)
    }
    
    func getNewsFeeds(fetched: Bool) {
        do {
            try self.nativeViewModel.getNewsFeed(load: true, onLoading: {
                self.launches = LoadableLaunches.loading
            }, onSuccess: { news in
                self.launches = LoadableLaunches.result(news)
            }, onError: { error in
                self.launches = LoadableLaunches.error(error)
            }, onEmpty: {
                self.launches = LoadableLaunches.result([])
            })
        } catch  {
            print("Error from do and catch scope")
        }
    }
    
    func cancelJob() {
        self.nativeViewModel.onDestroy()
    }
}
