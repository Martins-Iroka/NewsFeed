//
//  WebView.swift
//  NewsFeed
//
//  Created by Martins on 4/20/21.
//

import SwiftUI
import WebKit

struct WebView : UIViewRepresentable {
    
    let url: String
    @StateObject var viewModel: NewsFeedViewModel
    let webView = WKWebView()
    
    func makeCoordinator() -> Coordinator {
        Coordinator(self.viewModel)
    }
    
    class Coordinator: NSObject, WKNavigationDelegate {
        var viewModel: NewsFeedViewModel
        
        init(_ viewModel: NewsFeedViewModel) {
            self.viewModel = viewModel
        }
        
        func webView(_ webView: WKWebView, didFinish navigation: WKNavigation!) {
            self.viewModel.webViewLoading = false
        }
    }
    
    func updateUIView(_ uiView: WKWebView, context: Context) {}
    
    
    func makeUIView(context: Context) -> WKWebView  {
        self.webView.navigationDelegate = context.coordinator
        if let url = URL(string: url) {
            let request = URLRequest(url: url)
            self.webView.load(request)
        }
        return self.webView
    }
}


struct ActivityIndicatorView: UIViewRepresentable {
    @Binding var webViewLoading: Bool
    let style: UIActivityIndicatorView.Style
    
    func makeUIView(context: UIViewRepresentableContext<ActivityIndicatorView>) -> UIActivityIndicatorView {
        return UIActivityIndicatorView(style: style)
    }
    
    func updateUIView(_ uiView: UIActivityIndicatorView, context: UIViewRepresentableContext<ActivityIndicatorView>) {
        webViewLoading ? uiView.startAnimating() : uiView.stopAnimating()
    }
}

struct LoadingView<Content>: View where Content: View {
    @StateObject var viewModel: NewsFeedViewModel
    var content: () -> Content
    
    var body: some View {
        
        ZStack(alignment: .center) {
            self.content()
                .disabled(self.viewModel.webViewLoading)
                .blur(radius: self.viewModel.webViewLoading ? 3 : 0)
        
            ActivityIndicatorView(webViewLoading: self.$viewModel.webViewLoading, style: .large)
        
        }
    }
}
