//
//  PontoApp.swift
//  Ponto
//
//  Created by Joao Victor Heringer on 26/08/25.
//

import ComposeApp
import SwiftUI

@main
struct PontoApp: App {
    init() {
        StartKoinKt.startIosKoin()
    }
    var body: some Scene {
        WindowGroup {
            ContentView()
        }
    }
}
