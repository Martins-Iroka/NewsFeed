//
//  NewsItemView.swift
//  NewsFeed
//
//  Created by TellerOne on 4/20/21.
//

import SwiftUI
import shared

struct NewsItemView: View {
    var newsFeed: NewsFeed
    var body: some View {
        HStack {
            ImageView(withURL: newsFeed.urlToImage ?? "", width: 100, height: 100)
            VStack(alignment:.leading) {
                Text(newsFeed.author)
                    .font(.headline)
                Text(newsFeed.title)
                    .font(.subheadline)
            }
            .padding(.bottom, 50)
            Spacer()
        }
    }
}

struct NewsItemView_Previews: PreviewProvider {
    static var previews: some View {
        NewsItemView(
            newsFeed: NewsFeed(id: 0, author: "IK", title: "The faith of the world", urlToImage: "Profile_Picture", url: "Hello world")
        )
    }
}
